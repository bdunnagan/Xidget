/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ISingleValueUpdateFeature suitable for most purposes.
 */
public class SingleValueModelFeature implements ISingleValueModelFeature
{
  public SingleValueModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setStorageLocation(org.xmodel.IModelObject)
   */
  @Override
  public void setSourceNode( IModelObject newNode)
  {
    IModelObject oldNode = node;
    if ( oldNode == newNode) return;
    node = newNode;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
    this.variable = name;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    if ( node != null) return node.getValue();
    
    if ( variable != null)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      StatefulContext context = bindFeature.getBoundContext();
      Object value = context.get( variable);
      if ( !(value instanceof List)) return value;
    }
    
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    if ( node != null) node.setValue( value);
    
    if ( variable != null)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      StatefulContext context = bindFeature.getBoundContext();
      context.getScope().set( variable, value);
    }
  }

  protected IXidget xidget;
  private IModelObject node;
  private String variable;
}
