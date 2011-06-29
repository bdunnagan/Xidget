/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

/**
 * An interface for getting and setting a single value in a widget.
 */
public interface ISingleValueWidgetFeature
{
  /**
   * Set the value in the widget.
   * @param value The value.
   * @return Returns the previous value.
   */
  public Object setValue( Object value);
  
  /**
   * @return Returns the value shown in the widget.
   */
  public Object getValue();
}
