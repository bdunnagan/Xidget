/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.graph;

import org.xidget.IXidget;

/**
 * An interface for defining the graph associated with a scale xidget.
 */
public interface IScaleFeature
{
  /**
   * Set the axis that this scale xidget represents on the specified graph.
   * @param axis The axis.
   * @param xidget The graph xidget.
   */
  public void setGraph( String axis, IXidget xidget);
}
