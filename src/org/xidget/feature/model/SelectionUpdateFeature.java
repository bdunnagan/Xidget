/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Reference;
import org.xmodel.Xlate;
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
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#setMode(org.xidget.ifeature.model.ISelectionUpdateFeature.Mode)
   */
  @Override
  public void setMode( Mode mode)
  {
    this.mode = mode;
    if ( mode != Mode.ref) fkmap = new HashMap<String, IModelObject>();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#updateWidget()
   */
  @Override
  public void updateWidget()
  {
    if ( updating) return;
    updating = true;
    
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
        if ( !rhsSet.contains( toModel( lObject)))
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
        rObject = toDisplay( rObject);
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
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#updateModel()
   */
  @Override
  public void updateModel()
  {
    if ( updating) return;
    updating = true;
    
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
        if ( !rhsSet.contains( toDisplay( lObject)))
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
        rObject = toModel( rObject);
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
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#displaySelect(java.util.List)
   */
  @Override
  public void displaySelect( List<? extends Object> objects)
  {
    if ( updating) return;
    updating = true;
    
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
        object = toDisplay( object);
        if ( onSelect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onSelect", context);
        }
        widgetFeature.select( object);
        if ( fkmap != null) fkmap.put( ((IModelObject)object).getID(), (IModelObject)object);
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#displayDeselect(java.util.List)
   */
  @Override
  public void displayDeselect( List<? extends Object> objects)
  {
    if ( updating) return;
    updating = true;
    
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
        object = toDisplay( object);
        if ( onDeselect) 
        {
          context.getScope().set( "value", object);
          scriptFeature.runScript( "onDeselect", context);
        }
        widgetFeature.deselect( object);
        if ( fkmap != null) fkmap.remove( ((IModelObject)object).getID());
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#modelSelect(java.util.List)
   */
  @Override
  public void modelSelect( List<? extends Object> objects)
  {
    if ( updating) return;
    updating = true;
    
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
        modelFeature.select( toModel( object));
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#modelDeselect(java.util.List)
   */
  @Override
  public void modelDeselect( List<? extends Object> objects)
  {
    if ( updating) return;
    updating = true;
    
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
        modelFeature.deselect( toModel( object));
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#toDisplay(java.lang.Object)
   */
  @Override
  public Object toDisplay( Object selected)
  {
    switch( mode)
    {
      case ref:
      {
        return ((IModelObject)selected).getReferent();
      }
      
      case fk1:
      {
        IModelObject displayNode = fkmap.get( Xlate.get( (IModelObject)selected, (String)null));
        if ( displayNode == null) throw new IllegalStateException();
        return displayNode;
      }
      
      case fk2:
      {
        IModelObject displayNode = fkmap.get( Xlate.get( (IModelObject)selected, "id", (String)null));
        if ( displayNode == null) throw new IllegalStateException();
        return displayNode;
      }
    }
    
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionUpdateFeature#toModel(java.lang.Object)
   */
  @Override
  public Object toModel( Object selected)
  {
    switch( mode)
    {
      case ref:
      {
        return new Reference( (IModelObject)selected);
      }
      
      case fk1:
      {
        IModelObject element = (IModelObject)selected;
        IModelObject fk = element.createObject( element.getType());
        fk.setValue( element.getID());
        return fk;
      }
      
      case fk2:
      {
        IModelObject element = (IModelObject)selected;
        IModelObject fk = element.createObject( element.getType());
        fk.setID( element.getID());
        return fk;
      }
    }
    
    return null;
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
  private boolean updating;
  private Mode mode;
  private Map<String, IModelObject> fkmap;
}
