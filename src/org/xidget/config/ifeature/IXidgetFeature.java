/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.ifeature;

import org.xidget.IXidget;

/**
 * An interface for accessing an instance of IXidget created by an ITagHandler.
 * This feature is for use on ITagHandler.
 */
public interface IXidgetFeature
{
  /**
   * Returns the last xidget created by the ITagHandler.
   * @return Returns the last xidget created by the ITagHandler.
   */
  public IXidget getXidget();
}
