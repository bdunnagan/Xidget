/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * The standard implementation of ISelectionUpdateFeature that extends SingleValueUpdateFeature
 * so that it can use the same transformation semantics.  Each value that is part of the selection 
 * is transformed first.
 */
public class SelectionUpdateFeature implements ISelectionUpdateFeature
{
  public SelectionUpdateFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#updateWidget()
   */
  @Override
  public void updateWidget()
  {
    if ( updatingWidget) return;
    updatingWidget = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get model selection
      ISelectionModelFeature modelFeature = xidget.getFeature( ISelectionModelFeature.class);
      List<? extends Object> rhs = modelFeature.getSelection();
      Set<Object> rhsSet = new HashSet<Object>( rhs);
      
      // get widget selection
      ISelectionWidgetFeature widgetFeature = xidget.getFeature( ISelectionWidgetFeature.class);
      List<? extends Object> lhs = widgetFeature.getSelection();
      Set<Object> lhsSet = new HashSet<Object>( lhs);
      
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onSelect = context != null && scriptFeature.hasScript( "onSelect");
      boolean onDeselect = context != null && scriptFeature.hasScript( "onDeselect");
      
      // handle deletions
      for( Object lObject: lhsSet)
      {
        if ( !rhsSet.contains( lObject))
        {
          if ( onDeselect) 
          {
            context.getScope().set( "value", lObject);
            scriptFeature.runScript( "onDeselect", context);
          }
          widgetFeature.deselect( lObject);
        }
      }
      
      // handle insertions
      for( Object rObject: rhsSet)
      {
        if ( !lhsSet.contains( rObject))
        {
          if ( onSelect) 
          {
            context.getScope().set( "value", rObject);
            scriptFeature.runScript( "onSelect", context);
          }
          widgetFeature.select( rObject);
        }
      }
    }
    finally
    {
      updatingWidget = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#updateModel()
   */
  @Override
  public void updateModel()
  {
    if ( updatingModel) return;
    updatingModel = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get widget selection
      ISelectionWidgetFeature widgetFeature = xidget.getFeature( ISelectionWidgetFeature.class);
      List<? extends Object> rhs = widgetFeature.getSelection();
      Set<Object> rhsSet = new HashSet<Object>( rhs);
      
      // get model selection
      ISelectionModelFeature modelFeature = xidget.getFeature( ISelectionModelFeature.class);
      List<? extends Object> lhs = modelFeature.getSelection();
      Set<Object> lhsSet = new HashSet<Object>( lhs);
  
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onSelect = context != null && scriptFeature.hasScript( "onSelect");
      boolean onDeselect = context != null && scriptFeature.hasScript( "onDeselect");
      
      // handle deletions
      for( Object lObject: lhsSet)
      {
        if ( !rhsSet.contains( lObject))
        {
          if ( onDeselect) 
          {
            context.getScope().set( "value", lObject);
            scriptFeature.runScript( "onDeselect", context);
          }
          modelFeature.deselect( lObject);
        }
      }
      
      // handle insertions
      for( Object rObject: rhsSet)
      {
        if ( !lhsSet.contains( rObject))
        {
          if ( onSelect) 
          {
            context.getScope().set( "value", rObject);
            scriptFeature.runScript( "onSelect", context);
          }
          modelFeature.select( rObject);
        }
      }
    }
    finally
    {
      updatingModel = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#displaySelect(java.util.List)
   */
  @Override
  public void displaySelect( List<? extends Object> objects)
  {
    if ( updatingWidget) return;
    updatingWidget = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onSelect = context != null && scriptFeature.hasScript( "onSelect");
  
      // update
      ISelectionWidgetFeature widgetFeature = xidget.getFeature( ISelectionWidgetFeature.class);
      for( Object object: objects)
      {
        if ( onSelect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onSelect", context);
        }
        widgetFeature.select( object);
      }
    }
    finally
    {
      updatingWidget = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#displayDeselect(java.util.List)
   */
  @Override
  public void displayDeselect( List<? extends Object> objects)
  {
    if ( updatingWidget) return;
    updatingWidget = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onDeselect = context != null && scriptFeature.hasScript( "onDeselect");
  
      // update
      ISelectionWidgetFeature widgetFeature = xidget.getFeature( ISelectionWidgetFeature.class);
      for( Object object: objects)
      {
        if ( onDeselect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onDeselect", context);
        }
        widgetFeature.deselect( object);
      }
    }
    finally
    {
      updatingWidget = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#modelSelect(java.util.List)
   */
  @Override
  public void modelSelect( List<? extends Object> objects)
  {
    if ( updatingModel) return;
    updatingModel = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onSelect = context != null && scriptFeature.hasScript( "onSelect");
  
      // update
      ISelectionModelFeature modelFeature = xidget.getFeature( ISelectionModelFeature.class);
      for( Object object: objects)
      {
        if ( onSelect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onSelect", context);
        }
        modelFeature.select( object);
      }
    }
    finally
    {
      updatingModel = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#modelDeselect(java.util.List)
   */
  @Override
  public void modelDeselect( List<? extends Object> objects)
  {
    if ( updatingModel) return;
    updatingModel = true;
    
    try
    {
      StatefulContext context = getContext();
      if ( context == null) return;
        
      // get script feature
      IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
      boolean onDeselect = context != null && scriptFeature.hasScript( "onDeselect");
  
      // update
      ISelectionModelFeature modelFeature = xidget.getFeature( ISelectionModelFeature.class);
      for( Object object: objects)
      {
        if ( onDeselect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onDeselect", context);
        }
        modelFeature.deselect( object);
      }
    }
    finally
    {
      updatingModel = false;
    }
  }
  
  /**
   * Returns the context for script execution and/or transformation.
   * @return Returns the context for script execution and/or transformation.
   */
  protected StatefulContext getContext()
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    return bindFeature.getBoundContext();
  }
  
  protected IXidget xidget;
  private boolean updatingModel;
  private boolean updatingWidget;
}
