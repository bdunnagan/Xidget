/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xidget.chart.Point;

/**
 * An interface for specifying points in a point list.
 */
public interface IPointsFeature
{
  /**
   * Add a point to the end of the list.
   * @param point The point.
   */
  public void add( Point point);
  
  /**
   * Insert a point at the specified index.
   * @param index The index.
   * @param point The point.
   */
  public void add( int index, Point point);

  /**
   * Update the specified point.
   * @param point The point.
   * @param coordinate The coordinate.
   * @param value The new value.
   */
  public void update( Point point, int coordinate, double value);
  
  /**
   * Update the specified point.
   * @param point The point.
   * @param label The new label.
   */
  public void update( Point point, String label);
  
  /**
   * Remove the point at the specified index.
   * @param index The index.
   */
  public void remove( int index);
}