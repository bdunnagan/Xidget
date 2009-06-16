/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.button;

/**
 * An interface for operating on a button widget.
 */
public interface IButtonWidgetFeature
{
  /**
   * Set the button state (only applies to toggle and checkbox buttons).
   * @param state The state.
   */
  public void setState( boolean state);
}
