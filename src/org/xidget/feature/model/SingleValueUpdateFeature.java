/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xidget.ifeature.model.ISingleValueWidgetFeature;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of the single-value transformation and validation pipeline.
 */
public class SingleValueUpdateFeature implements ISingleValueUpdateFeature
{
  public SingleValueUpdateFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#isUpdating()
   */
  @Override
  public boolean isUpdating()
  {
    return updating;
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
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setDisplayScript(org.xmodel.xaction.ScriptAction)
   */
  @Override
  public void setDisplayScript( ScriptAction script)
  {
    displayScript = script;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setCommitScript(org.xmodel.xaction.ScriptAction)
   */
  @Override
  public void setCommitScript( ScriptAction script)
  {
    commitScript = script;
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
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setValueInModel(java.lang.Object)
   */
  @Override
  public void commit( Object value)
  {
    if ( updating) return;
    updating = true;
    
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = new StatefulContext( bindFeature.getBoundContext());

    try
    {
      // transform
      if ( displayExpr != null)
      {
        value = performTransform( context, displayExpr, value);
      }
      
      // execute script
      if ( displayScript != null)
      {
        value = executeScript( context, displayScript, value);
      }
      
      // set
      ISingleValueModelFeature modelFeature = xidget.getFeature( ISingleValueModelFeature.class);
      modelFeature.setValue( value);
    }
    finally
    {
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueUpdateFeature#setValueInWidget(java.lang.Object)
   */
  @Override
  public void display( Object value)
  {
    if ( updating) return;
    updating = true;

    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = new StatefulContext( bindFeature.getBoundContext());
      
    try
    {
      // transform
      if ( commitExpr != null)
      {
        context.getScope().set( "value", value);
        value = performTransform( context, commitExpr, value);
      }
      
      // execute script
      if ( commitScript != null)
      {
        context.getScope().set( "value", value);
        value = executeScript( context, commitScript, value);
      }
      
      // set
      ISingleValueWidgetFeature widgetFeature = xidget.getFeature( ISingleValueWidgetFeature.class);
      widgetFeature.setValue( value);
    }
    finally
    {
      updating = false;
    }
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
  
  /**
   * Transform and/or validate the specified value.
   * @param context The context.
   * @param script The validation script.
   * @param value The value.
   * @return Returns null (veto) or the optionally transformed value.
   */
  private Object executeScript( StatefulContext context, ScriptAction script, Object value)
  {
    Object[] result = script.run( context);
    if ( result == null || result.length == 0) return null;
    return result[ 0];
  }
  
  private IXidget xidget;
  private ScriptAction commitScript;
  private ScriptAction displayScript;
  private IExpression commitExpr;
  private IExpression displayExpr;
  private boolean updating;
}
