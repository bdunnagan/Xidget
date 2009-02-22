/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.widget;

/**
 * An interface for all widgets used by IXidget implementations. Instances of this interface
 * and its sub-interfaces are the contract by which IXidget implementations manipulate their
 * associated widgets.
 */
public interface IWidgetAdapter
{
  /**
   * Set whether the widget is enabled.
   * @param enabled True if enabled.
   */
  public void setEnabled( boolean enabled);
}
