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
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.selection.AbstractSelectionDiffer;
import org.xidget.selection.Change;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of ISelectionModelFeature that diffs new nodes into the selection parent.
 */
public abstract class SelectionModelFeature implements ISelectionModelFeature
{
  public SelectionModelFeature( IXidget xidget, AbstractSelectionDiffer differ)
  {
    this.xidget = xidget;
    this.differ = differ;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setParent(org.xmodel.xpath.expression.StatefulContext, org.xmodel.IModelObject)
   */
  public void setParent( StatefulContext context, IModelObject element)
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
   * @see org.xidget.ifeature.ISelectionModelFeature#setFilter(org.xmodel.xpath.expression.IExpression)
   */
  public void setFilter( IExpression filter)
  {
    this.filter = filter; 
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setSelection(Object, java.util.List)
   */
  public void setSelection( Object widget, List<IModelObject> nodes)
  {
    if ( parent == null || updating) return;

    // filter
    IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
    StatefulContext context = contextFeature.getContext( widget);
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
   * @see org.xidget.ifeature.ISelectionModelFeature#getSelection(java.lang.Object)
   */
  public List<IModelObject> getSelection( Object widget)
  {
    if ( parent == null) return Collections.emptyList();
    
    IWidgetContextFeature contextFeature = xidget.getFeature( IWidgetContextFeature.class);
    StatefulContext context = contextFeature.getContext( widget);
    return applyFilter( context, parent.getChildren());
  }
  
  /**
   * Returns a new pointer to the specified node. The pointer can be any type of element
   * which contains enough information to identify the specified node.
   * @param node The node to be referenced.
   * @return Returns a new pointer to the specified node.
   */
  protected abstract IModelObject createReference( IModelObject node);
  
  /**
   * Returns the filtered set of nodes.
   * @param context The parent context of filter evaluation.
   * @param nodes The nodes to be filtered.
   * @return Returns the filtered set of nodes.
   */
  protected List<IModelObject> applyFilter( StatefulContext context, List<IModelObject> nodes)
  {
    if ( filter == null) return nodes;

    List<IModelObject> filtered = new ArrayList<IModelObject>( nodes.size());
    for( IModelObject node: nodes)
      if ( filter.evaluateBoolean( new StatefulContext( context, node)))
        filtered.add( node);
    
    return filtered;
  }
  
  protected IXidget xidget;
  private AbstractSelectionDiffer differ;
  private IModelObject parent;
  private IExpression filter;
  private boolean updating;
}
