/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.chart;

import org.xidget.chart.Plot;
import org.xidget.chart.Point;

/**
 * An interface for xidgets that visualize one or more ordered lists of points.  Points are organized into
 * plots.  Each plot may have different visual characteristics.
 */
public interface IPlotFeature
{
  /**
   * Called when a new plot is added.
   * @param plot The plot.
   */
  public void add( Plot plot);
  
  /**
   * Called when a plot is removed.
   * @param plot The plot.
   */
  public void remove( Plot plot);
  
  /**
   * Called when a point is added to a plot.
   * @param plot The plot containing the point.
   * @param index The index where the point was inserted.
   * @param point The point.
   */
  public void add( Plot plot, int index, Point point);
  
  /**
   * Called when a point is removed from a plot
   * @param plot The plot containing the point.
   * @param index The index of the point that is being removed.
   */
  public void remove( Plot plot, int index);
  
  /**
   * Called when any attribute of a point is updated.
   * @param plot The plot containing the point.
   * @param point The point that was updated.
   */
  public void update( Plot plot, Point point);
}