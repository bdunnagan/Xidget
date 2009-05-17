/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.button;

import org.xidget.ifeature.ISourceFeature;

/**
 * An interface for operating on the button model.
 */
public interface IButtonModelFeature extends ISourceFeature
{
  /**
   * Set the button state.
   * @param state The state.
   */
  public void setState( boolean state);
}
