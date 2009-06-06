/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.XidgetSwitch;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of ITreeExpandFeature. This class is responsible for binding
 * and unbinding the correct sub-tree xidget to the row context when the row is expanded
 * and collapsed, respectively.  This feature also listens to the dirty state of the
 * element to determine when a temporary child should be added so that dirty rows can
 * be expanded, and therefore synced. 
 */
public class TreeExpandFeature implements ITreeExpandFeature
{
  public TreeExpandFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.dummy = new StatefulContext( new ModelObject( "dummy"));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#rowAdded(org.xidget.table.Row)
   */
  public void rowAdded( Row row)
  {
    if ( !row.isExpanded()) fakeExpand( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#rowRemoved(org.xidget.table.Row)
   */
  public void rowRemoved( Row row)
  {
    if ( row.isExpanded()) realCollapse( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#expand(org.xidget.table.Row)
   */
  public void expand( Row row)
  {
    Log.printf( "xidget.tree", "%s: expand: %s\n", hashCode(), row);
    
    // remove fake content
    fakeCollapse( row);
    
    // expand real content for row
    realExpand( row);
    
    // fake expand children
    for( Row child: row.getChildren())
      fakeExpand( child);
    
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.commit( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#collapse(org.xidget.table.Row)
   */
  public void collapse( Row row)
  {
    Log.printf( "xidget.tree", "%s: collapse: %s\n", hashCode(), row);
    
    // fake collapse children
    for( Row child: row.getChildren())
      fakeCollapse( child);
    
    // remove real content for row
    realCollapse( row);
    
    // fake expand row
    Row parent = row.getParent();
    if ( parent != null && parent.isExpanded())
      fakeExpand( row);
  }
  
  /**
   * Expand the specified row with its real content.
   * @param row The row.
   */
  private void realExpand( Row row)
  {
    if ( row.isExpanded()) return;
    row.setExpanded( true);

    Log.printf( "xidget.tree", "%s: real expand: %s\n", hashCode(), row);
    
    // bind subtree to get real content
    XidgetSwitch treeSwitch = getSwitch( row);
    treeSwitch.bind( row.getContext());
  }
  
  /**
   * Collapse the specified row removing its real content. 
   * @param row The row.
   */
  private void realCollapse( Row row)
  {
    if ( !row.isExpanded()) return;
    row.setExpanded( false);
   
    Log.printf( "xidget.tree", "%s: real collapse: %s\n", hashCode(), row);
    
    // unbind subtree to remove real content
    XidgetSwitch treeSwitch = getSwitch( row);
    treeSwitch.unbind( row.getContext());
  }
  
  /**
   * Expand the specified row with a temporary child if the contents are not yet known.
   * @param row The row.
   */
  private void fakeExpand( Row row)
  {
    Log.printf( "xidget.tree", "%s: fake expand: %s\n", hashCode(), row);
    
    if ( needsTemporaryChild( row))
    {
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      
      Row temp = new Row( null);
      temp.setContext( dummy);
      temp.getCell( 0).text = "Oops!";
      row.addChild( 0, 0, temp);
      
      feature.insertRows( row, 0, new Row[] { temp});
    }
  }
  
  /**
   * Collapse the specified row and remove its temporary child if present.
   * @param row The row.
   */
  private void fakeCollapse( Row row)
  {
    Log.printf( "xidget.tree", "%s: fake collapse: %s\n", hashCode(), row);
    
    // remove temporary node from table zero
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    List<Row> children = row.getChildren( 0);
    for( int i=0; i<children.size(); i++)
    {
      Row child = children.get( i);
      if ( child.getContext() == dummy)
      {
        row.removeChild( 0, i);
        feature.removeRows( row, i, new Row[] { child}); 
        break;
      }
    }
  }
  
  /**
   * Returns true if the specified row needs a temporary child.
   * @param row The row.
   * @return Returns true if the specified row needs a temporary child.
   */
  private boolean needsTemporaryChild( Row row)
  {
    // 1. parent must be expanded
    Row parent = row.getParent();
    if ( parent != null && !parent.isExpanded()) return false;
    
    // 2. at least one case in xidget switch
    XidgetSwitch treeSwitch = getSwitch( row);
    if ( treeSwitch.getCaseCount() == 0) return false;
    
    // 3. row object is dirty
    //StatefulContext context = row.getContext();
    //if ( context == null || !context.getObject().isDirty()) return false;
    
    // 4. row is not expanded
    if ( row.isExpanded()) return false;
    
    // 5. row has no children
    return row.getChildCount() == 0; 
  }
  
  /**
   * Find the tree xidgets associated with the table from which the specified row comes.
   * @param row The row being expanded or collapsed.
   * @return Returns a list of tree xidgets.
   */
  private List<IXidget> findTree( Row row)
  {
    List<IXidget> trees = new ArrayList<IXidget>();
    
    IXidget table = row.getTable();
    if ( table == null) return Collections.emptyList();;
    
    // the table that generated the row
    IModelObject tableElement = table.getConfig();
    
    // the tree containing the table that generated the row
    IModelObject treeElement = tableElement.getParent();
    
    // get all candidate tree declarations
    List<IXidget> candidates = findTreeCandidates( row);
    for( IXidget child: candidates)
    {
      IModelObject element = child.getConfig();
      IExpression parentExpr = Xlate.get( element, "parent", (IExpression)null);
      List<IModelObject> result = parentExpr.query( treeElement, null);
      if ( result.contains( tableElement)) trees.add( child); 
    }
    
    return trees;
  }
  
  /**
   * Returns all the candidate tree declarations. The candidate tree declarations are those
   * declarations defined as peers of the table that generated the specified row, the child
   * declarations of the ancestors of the table that generated the specified row.  That is,
   * all tree declarations reachable from any ancestor of the table that generated the 
   * specified row. 
   * @return Returns all the candidate tree declarations.
   */
  private List<IXidget> findTreeCandidates( Row row)
  {
    IXidget table = row.getTable();
    if ( table == null) return Collections.emptyList();;
    
    List<IXidget> candidates = new ArrayList<IXidget>();
    
    // all children of parent are candidates
    IXidget parent = table.getParent();
    for( IXidget child: parent.getChildren())
    {
      IModelObject element = child.getConfig();
      if ( element.isType( parent.getConfig().getType()))
        candidates.add( child);
    }
    
    // all children of ancestors marked as recursive are candidates
    if ( parent != null)
    {
      IXidget ancestor = parent.getParent();
      while( ancestor != null && ancestor.getFeature( ITreeWidgetFeature.class) != null)
      {
        for( IXidget child: ancestor.getChildren())
        {
          IModelObject element = child.getConfig();
          if ( Xlate.get( element, "recursive", false) && element.isType( ancestor.getConfig().getType()))
            candidates.add( child);
        }
        ancestor = ancestor.getParent();
      }
    }
    
    return candidates;
  }
  
  /**
   * Returns the tree switch for the specified row.
   * @param row The row.
   * @return Returns the tree switch for the specified row.
   */
  private XidgetSwitch getSwitch( Row row)
  {
    if ( row.getSwitch() == null)
    {
      XidgetSwitch treeSwitch = new XidgetSwitch();
      List<IXidget> xidgets = findTree( row);
      for( IXidget xidget: xidgets)
      {
        IExpression condition = Xlate.get( xidget.getConfig(), "when", (IExpression)null);
        if ( condition != null) treeSwitch.addCase( condition, xidget);
      }
      row.setSwitch( treeSwitch);
    }
    
    return row.getSwitch();
  }
  
  protected IXidget xidget;
  private StatefulContext dummy;
}
