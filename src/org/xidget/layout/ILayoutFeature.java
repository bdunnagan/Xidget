/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An interface for the layout algorithm feature. This feature is responsible
 * for computing new positions for widgets as a result of a change in the
 * position of a particular widget. This feature is associated with xidgets
 * that represent containers.
 */
public interface ILayoutFeature
{
  /**
   * Layout the children of the container.
   */
  public void layout();
  
  /**
   * Set the layout for the specified xidget.
   * @param processor The tag processor.
   * @param xidget The xidget.
   * @param element The layout element.
   */
  public void setLayout( TagProcessor processor, IXidget xidget, IModelObject element);
}
