/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.XidgetSwitch;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

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
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#rowAdded(org.xidget.table.Row)
   */
  public void rowAdded( Row row)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#rowRemoved(org.xidget.table.Row)
   */
  public void rowRemoved( Row row)
  {
    if ( row.isExpanded()) collapse( row);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#expand(org.xidget.table.Row)
   */
  public void expand( Row row)
  {
    row.setExpanded( true);
    
    try
    {
      // bind tree switch
      XidgetSwitch treeSwitch = getSwitch( row);
      treeSwitch.bind( row.getContext());
    }
    finally
    {
      // remove temporary child
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      List<Row> children = row.getChildren();
      for( int i=0; i<children.size(); i++)
      {
        Row child = children.get( i);
        if ( child.getTable() == null)
        {
          feature.removeRows( row, i, new Row[] { child});
          break;
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#collapse(org.xidget.table.Row)
   */
  public void collapse( Row row)
  {
    row.setExpanded( false);
    if ( Xlate.get( xidget.getConfig(), "optimize", "speed").equals( "space"))
    {
      XidgetSwitch treeSwitch = getSwitch( row);
      treeSwitch.unbind( row.getContext());
    }
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
    
    IXidget ancestor = table.getParent();
    while( ancestor != null && ancestor.getFeature( ITreeWidgetFeature.class) != null)
    {
      for( IXidget child: ancestor.getChildren())
      {
        if ( child.getConfig().isType( ancestor.getConfig().getType()))
          candidates.add( child);
      }
      ancestor = ancestor.getParent();
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
}
