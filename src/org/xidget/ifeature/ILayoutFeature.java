/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the layout algorithm feature. This feature is responsible
 * for computing new positions for widgets as a result of a change in the
 * position of a particular widget. This feature is associated with xidgets
 * that represent containers.
 */
public interface ILayoutFeature
{
  /**
   * Configure the layout from the layout script or attachment declarations.
   * This method will reevaluate the configuration when called more than once.
   */
  public void configure();
  
  /**
   * Layout the children of the container.
   * @param context The widget context.
   */
  public void layout( StatefulContext context);
}
