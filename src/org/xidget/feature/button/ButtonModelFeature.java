/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.button;

import org.xidget.ifeature.button.IButtonModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * A default implementation of button model feature.
 */
public class ButtonModelFeature implements IButtonModelFeature
{
  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonModelFeature#setSource(org.xmodel.IModelObject)
   */
  public void setSource( IModelObject node)
  {
    this.node = node;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonModelFeature#getSource()
   */
  public IModelObject getSource()
  {
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonModelFeature#setState(boolean)
   */
  public void setState( boolean state)
  {
    if ( node != null) Xlate.set( node, state);
  }
  
  private IModelObject node;
}
