/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.graph;

import org.xidget.IXidget;

/**
 * An interface for defining the horizontal or vertical scale to be used for an
 * axis of a graph xidget. A graph xidget may plot more than one point list. In
 * this case, each point list may have different axes and corresponding scales.
 */
public interface IGraphFeature
{
  /**
   * Set the scale xidget for the specified axis. The axis identifies the name of one
   * of the coordinate elements in the points declaration.
   * @param axis The name of the axis.
   * @param xidget The horizontal or vertical scale xidget.
   */
  public void setScale( String axis, IXidget xidget);
}
