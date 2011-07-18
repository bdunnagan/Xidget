/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * The standard implementation of the ISingleValueUpdateFeature for the transformation and validation pipeline. 
 */
public class SingleValueUpdateFeature implements ISingleValueUpdateFeature
{
  public SingleValueUpdateFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#updateModel()
   */
  @Override
  public void updateModel()
  {
    ISingleValueWidgetFeature feature = xidget.getFeature( ISingleValueWidgetFeature.class);
    commit( feature.getValue());
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#updateWidget()
   */
  @Override
  public void updateWidget()
  {
    ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
    display( feature.getValue());
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setDisplayTransform(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setDisplayTransform( IExpression expression)
  {
    displayExpr = expression;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setCommitTransform(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setCommitTransform( IExpression expression)
  {
    commitExpr = expression;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#commit(java.lang.Object)
   */
  @Override
  public void commit( Object value)
  {
    value = toModel( value);
    if ( value != null)
    {
      ISingleValueModelFeature modelFeature = xidget.getFeature( ISingleValueModelFeature.class);
      Object oldValue = modelFeature.getValue();
      if ( oldValue == null || value == null)
      {
        if ( oldValue != value) modelFeature.setValue( value);
      }
      else if ( !oldValue.equals( value))
      {
        modelFeature.setValue( value);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setValueInWidget(java.lang.Object)
   */
  @Override
  public void display( Object value)
  {
    value = toDisplay( value);
    if ( value != null)
    {
      ISingleValueWidgetFeature widgetFeature = xidget.getFeature( ISingleValueWidgetFeature.class);
      Object oldValue = widgetFeature.getValue();
      if ( oldValue == null || value == null)
      {
        if ( oldValue != value) widgetFeature.setValue( value);
      }
      else if ( !oldValue.equals( value))
      {
        widgetFeature.setValue( value);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#toDisplay(java.lang.Object)
   */
  @Override
  public Object toDisplay( Object value)
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context == null) return null;
    
    // transform
    if ( displayExpr != null)
    {
      context.getScope().set( "value", value);
      value = performTransform( context, displayExpr, value);
    }
    
    // execute script
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    if ( scriptFeature.hasScript( "onGet"))
    {
      context.getScope().set( "value", value);
      Object[] result = scriptFeature.runScript( "onGet", context);
      value = (result != null && result.length > 0)? result[ 0]: null;
    }
    
    return value;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#toModel(java.lang.Object)
   */
  @Override
  public Object toModel( Object value)
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context == null) return null;
    
    // transform
    if ( commitExpr != null)
    {
      context.getScope().set( "value", value);
      value = performTransform( context, commitExpr, value);
    }
    
    // execute script
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    if ( scriptFeature.hasScript( "onSet"))
    {
      context.getScope().set( "value", value);
      Object[] result = scriptFeature.runScript( "onSet", context);
      value = (result != null && result.length > 0)? result[ 0]: null;
    }
    
    return value;
  }

  /**
   * Transform the specified value.
   * @param context The context.
   * @param expression The transform.
   * @param value The value.
   * @return Returns the transformed value.
   */
  private Object performTransform( StatefulContext context, IExpression expression, Object value)
  {
    switch( expression.getType( context))
    {
      case NUMBER:  return expression.evaluateNumber( context); 
      case BOOLEAN: return expression.evaluateBoolean( context);
      case NODES:   
      case STRING:  return expression.evaluateString( context);
    }
    return null;
  }
    
  protected IXidget xidget;
  private IExpression commitExpr;
  private IExpression displayExpr;
}
