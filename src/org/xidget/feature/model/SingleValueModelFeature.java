/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xmodel.IModelObject;

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
    
    ISingleValueUpdateFeature updateFeature = xidget.getFeature( ISingleValueUpdateFeature.class);
    updateFeature.updateWidget();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    if ( node == null) return null;
    return node.getValue();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    if ( node != null) node.setValue( value);
  }

  private IXidget xidget;
  private IModelObject node;
}
