/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

/**
 * An interface for creating and deleting the widget (or widgets) associated with a xidget.
 * It is usually convenient to implement this interface with another feature interface that
 * provides access to the widget that was created.
 */
public interface IWidgetCreationFeature
{
  /**
   * Create the widget or widgets for the associated xidget. This method
   * does not create widgets for the children of the xidget.
   */
  public void createWidget();
  
  /**
   * Destroy the widget or widgets.
   */
  public void destroyWidget();
}
