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
   * @see org.xidget.ifeature.ISourceFeature#setSource(org.xmodel.IModelObject)
   */
  public void setSource( IModelObject node)
  {
    this.node = node;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource()
   */
  public IModelObject getSource()
  {
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( String channel, IModelObject node)
  {
    throw new UnsupportedOperationException( "Buttons do not support channels.");
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    throw new UnsupportedOperationException( "Buttons do not support channels.");
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
