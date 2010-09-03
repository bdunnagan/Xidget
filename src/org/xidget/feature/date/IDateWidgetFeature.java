/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.date;

/**
 * An interface for a date picking widget.
 */
public interface IDateWidgetFeature
{
  /**
   * Set the selected time.
   * @param time The selected time.
   */
  public void setSelectedTime( long time);
  
  /**
   * Returns the selected time.
   * @return Returns the selected time.
   */
  public long getSelectedTime();
}
