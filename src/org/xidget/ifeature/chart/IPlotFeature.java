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
  public void addPlot( Plot plot);
  
  /**
   * Called when a plot is removed.
   * @param plot The plot.
   */
  public void removePlot( Plot plot);

  /**
   * Update the foreground color of the plot.
   * @param plot The plot.
   * @param color The foreground color.
   */
  public void updateForeground( Plot plot, String color);
  
  /**
   * Update the background color of the plot.
   * @param plot The plot.
   * @param color The background color.
   */
  public void updateBackground( Plot plot, String color);
  
  /**
   * Called when a point is added to a plot.
   * @param plot The plot containing the point.
   * @param index The index where the point was inserted.
   * @param point The point.
   */
  public void addPoint( Plot plot, int index, Point point);
  
  /**
   * Called when a point is removed from a plot
   * @param plot The plot.
   * @param index The index of the point that is being removed.
   */
  public void removePoint( Plot plot, int index);
  
  /**
   * Called when a specific attribute of a point is updated.
   * @param point The point that was updated.
   * @param coords The new coordinates of the point.
   */
  public void updateCoords( Point point, double[] coords);
  
  /**
   * Called when a specific attribute of a point is updated.
   * @param point The point that was updated.
   * @param coordinate The index of the coordinate that was updated.
   * @param value The new value of the coordinate.
   */
  public void updateCoord( Point point, int coordinate, double value);
  
  /**
   * Called when a specific attribute of a point is updated.
   * @param point The point that was updated.
   * @param label The updated label for the point.
   */
  public void updateLabel( Point point, String label);

  /**
   * Called when a specific attribute of a point is updated.
   * @param point The point that was updated.
   * @param fcolor The new foreground color.
   */
  public void updateForeground( Point point, String fcolor);
  
  /**
   * Called when a specific attribute of a point is updated.
   * @param point The point that was updated.
   * @param bcolor The new background color.
   */
  public void updateBackground( Point point, String bcolor);
}