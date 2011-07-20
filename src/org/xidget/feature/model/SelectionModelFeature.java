/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.Collections;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableListener;
import org.xmodel.xpath.variable.IVariableScope;

/**
 * The standard implementation of ISelectionModelFeature responsible for storing and retrieving the selection
 * from the assigned variable in the current context.
 */
public class SelectionModelFeature implements ISelectionModelFeature
{
  public SelectionModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
    StatefulContext context = getContext();
    
    // remove old listener
    if ( varName != null) context.getScope().removeListener( varName, context, variableListener);
    
    // set variable
    varName = name;
    
    // make sure variable is defined in this scope
    if ( !context.getScope().isDefined( varName))
    {
      context.getScope().set( varName, Collections.emptyList());
    }
    
    // add new listener
    System.out.printf( "bind: %s\n", name);
    if ( varName != null) context.getScope().addListener( varName, context, variableListener);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    if ( varName == null) return;
    
    StatefulContext context = getContext();
    if ( context != null) context.getScope().insert( varName, object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    if ( varName == null) return;
    
    StatefulContext context = getContext();
    if ( context != null) context.getScope().remove( varName, object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> list)
  {
    if ( varName == null) return;

    if ( list.size() == 0 || !(list.get( 0) instanceof IModelObject)) return;
    
    StatefulContext context = getContext();
    if ( context != null) context.getScope().set( varName, list);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#getSelection()
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<? extends Object> getSelection()
  {
    StatefulContext context = getContext();
    if ( context != null) 
    {
      List<? extends Object> list = (List<? extends Object>)context.getScope().get( varName);
      if ( list != null) return list;
    }
    return Collections.emptyList();
  }

  /**
   * @return Returns the context for script execution and/or transformation.
   */
  protected StatefulContext getContext()
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    return bindFeature.getBoundContext();
  }
  
  private IVariableListener variableListener = new IVariableListener() {
    public void notifyAdd( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displaySelect( nodes);
    }
    public void notifyRemove( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displayDeselect( nodes);
    }
    public void notifyChange( String name, IVariableScope scope, IContext context, String newValue, String oldValue)
    {
    }
    public void notifyChange( String name, IVariableScope scope, IContext context, Number newValue, Number oldValue)
    {
    }
    public void notifyChange( String name, IVariableScope scope, IContext context, Boolean newValue)
    {
    }
  };
  
  protected IXidget xidget;
  private String varName;
}
