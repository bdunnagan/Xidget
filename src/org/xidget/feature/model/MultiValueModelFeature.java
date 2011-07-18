/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.Collections;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.IMultiValueModelFeature;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Standard implementation of IMultiValueModelFeature.
 */
public class MultiValueModelFeature implements IMultiValueModelFeature
{
  public MultiValueModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#setSourceExpression(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public void setSourceExpression( IExpression expression)
  {
    this.expression = expression;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
    this.variable = name;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#insertValue(int, java.lang.Object)
   */
  @Override
  public void insertValue( int index, Object object)
  {
    if ( variable == null) return;
    
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context != null) context.getScope().insert( variable, object, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#updateValue(int, java.lang.Object)
   */
  @Override
  public void updateValue( int index, Object value)
  {
    if ( variable == null) return;
    
    removeValue( index);
    insertValue( index, value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#removeValue(int)
   */
  @Override
  public void removeValue( int index)
  {
    if ( variable == null) return;
    
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context != null) context.getScope().remove( variable, index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#setValues(java.util.List)
   */
  @Override
  public void setValues( List<? extends Object> list)
  {
    if ( variable == null) return;
    
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context != null) context.getScope().set( variable, list);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueModelFeature#getValues()
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<? extends Object> getValues()
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context == null) return Collections.emptyList();
    
    if ( expression != null) return expression.evaluateNodes( context);
    
    if ( variable == null) return Collections.emptyList();
    
    Object values = context.getScope().get( variable);
    if ( values instanceof List) return (List<? extends Object>)values;
    
    return Collections.singletonList( values);
  }

  protected IXidget xidget;
  private IExpression expression;
  private String variable;
}
