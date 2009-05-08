/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.button;

import org.xmodel.IModelObject;

/**
 * An interface for operating on the button model.
 */
public interface IButtonModelFeature
{
  /**
   * Set the button source node. The value of the source node is updated
   * with the state of a toggle or checkbox button.
   * @param node The source node.
   */
  public void setSource( IModelObject node);
  
  /**
   * Returns the button state source node.
   * @return Returns null or the button state source node.
   */
  public IModelObject getSource();
  
  /**
   * Set the button state.
   * @param state The state.
   */
  public void setState( boolean state);
}
