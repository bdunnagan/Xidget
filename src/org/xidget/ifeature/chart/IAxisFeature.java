/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.chart;

import org.xidget.IXidget;
import org.xidget.chart.Scale.Format;

/**
 * An interface for defining the graph associated with a scale xidget.
 */
public interface IAxisFeature
{
  /**
   * Set the axis that this scale xidget represents on the specified graph.
   * @param axis The axis.
   * @param xidget The graph xidget.
   */
  public void setGraph( String axis, IXidget xidget);
  
  /**
   * Set the minimum and maximum values of the axis.
   * @param min The minimum value.
   * @param max The maximum value.
   */
  public void setExtrema( double min, double max);
  
  /**
   * Set the log base for the axis.
   * @param base The log base (0 for linear).
   */
  public void setLogBase( int base);
  
  /**
   * Set the format for the axis labels.
   * @param format The format.
   */
  public void setFormat( Format format);
}
