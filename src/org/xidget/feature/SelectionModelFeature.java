/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.selection.AbstractSelectionDiffer;
import org.xidget.selection.Change;
import org.xidget.selection.ForeignKeyListDiffer;
import org.xidget.selection.ReferenceListDiffer;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of ISelectionModelFeature that diffs new nodes into the selection parent.
 */
public class SelectionModelFeature implements ISelectionModelFeature
{
  public SelectionModelFeature( IXidget xidget, StatefulContext context)
  {
    this.xidget = xidget;
    this.context = context;
    
    // configure
    configure( xidget.getConfig().getFirstChild( "selection"));
    
    // bind parent expression
    parentExpr.addNotifyListener( context, parentListener);
  }

  /**
   * Configure this feature from the specified element.
   * @param element The element.
   */
  protected void configure( IModelObject element)
  {
    parentExpr = Xlate.childGet( element, "parent", Xlate.get( element, "parent", (IExpression)null));
    filterExpr = Xlate.childGet( element, "filter", Xlate.get( element, "filter", (IExpression)null));
    
    // choose the differ
    String srcAttr = Xlate.childGet( element, "srcAttr", Xlate.get( element, "srcAttr", (String)null));
    String dstAttr = Xlate.childGet( element, "dstAttr", Xlate.get( element, "dstAttr", (String)null));
    if ( srcAttr == null && dstAttr == null)
    {
      differ = new ReferenceListDiffer( Xlate.get( element, "deference", "one").equals( "all"));
    }
    else
    {
      differ = new ForeignKeyListDiffer( srcAttr, dstAttr);
    }
  }
  
  /**
   * Set the selection parent element.
   * @param element The element.
   */
  public void setParent( IModelObject element)
  {
    parent = element;
    
    try
    {
      updating = true;
      
      // update widget selection
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      if ( feature != null) 
      {
        if ( parent != null) feature.setSelection( applyFilter( context, parent.getChildren()));
        else feature.setSelection( Collections.<IModelObject>emptyList());
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setSelection(java.util.List)
   */
  public void setSelection( List<IModelObject> nodes)
  {
    if ( parent == null || updating) return;

    // filter
    nodes = applyFilter( context, nodes);
    
    // diff
    differ.diff( parent.getChildren(), nodes);
    List<Change> changes = differ.getChanges();
    for( Change change: changes)
    {
      // add
      if ( change.rIndex >= 0)
        for( int i=0; i<change.count; i++)
        {
          parent.addChild( differ.createReference( nodes.get( change.rIndex)), change.lIndex + i);
        }
      
      // remove
      else
      {
        for( int i=0; i<change.count; i++)
          parent.removeChild( change.lIndex);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#getSelection()
   */
  public List<IModelObject> getSelection()
  {
    if ( parent == null) return Collections.emptyList();
    return applyFilter( context, parent.getChildren());
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#getIdentity(org.xmodel.IModelObject)
   */
  public Object getIdentity( IModelObject node)
  {
    return differ.getIdentity( node);
  }

  /**
   * Returns a new pointer to the specified node. The pointer can be any type of element
   * which contains enough information to identify the specified node.
   * @param node The node to be referenced.
   * @return Returns a new pointer to the specified node.
   */
  protected IModelObject createReference( IModelObject node)
  {
    return differ.createReference( node);
  }
  
  /**
   * Returns the filtered set of nodes.
   * @param context The parent context of filter evaluation.
   * @param nodes The nodes to be filtered.
   * @return Returns the filtered set of nodes.
   */
  protected List<IModelObject> applyFilter( StatefulContext context, List<IModelObject> nodes)
  {
    if ( filterExpr == null) return nodes;

    List<IModelObject> filtered = new ArrayList<IModelObject>( nodes.size());
    for( IModelObject node: nodes)
      if ( filterExpr.evaluateBoolean( new StatefulContext( context, node)))
        filtered.add( node);
    
    return filtered;
  }

  private final IExpressionListener parentListener = new ExpressionListener() {
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      if ( node != parent) setParent( node);
    }
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      if ( node != parent) setParent( node);
    }
    public boolean requiresValueNotification()
    {
      return false;
    }
  };
  
  protected IXidget xidget;
  private StatefulContext context;
  private AbstractSelectionDiffer differ;
  private IExpression filterExpr;
  private IExpression parentExpr;
  private IModelObject parent;
  private boolean updating;
}
