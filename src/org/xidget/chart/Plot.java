/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.util.List;

/**
 * A class that stores an ordered list of points and display characteristics.
 */
public class Plot
{
  /**
   * Add a point to the plot.
   * @param index The index of the point.
   * @param point The point.
   */
  public void add( int index, Point point)
  {
    if ( points.size() == 0)
    {
      min = new double[ point.coords.length];
      max = new double[ point.coords.length];
      System.arraycopy( point.coords, 0, min, 0, min.length);
      System.arraycopy( point.coords, 0, max, 0, max.length);
    }
    else
    {
      for( int i=0; i<point.coords.length; i++)
      {
        double coord = point.coords[ i];
        if ( coord < min[ i]) min[ i] = coord;
        if ( coord > max[ i]) max[ i] = coord;
      }
    }
  }

  /**
   * Remove a point from the plot.
   * @param index The index.
   */
  public void remove( int index)
  {
    Point point = points.remove( index);
    if ( isExtreme( point))
    {
      System.arraycopy( points.get( 0).coords, 0, min, 0, min.length);
      System.arraycopy( points.get( 0).coords, 0, max, 0, max.length);
      for( int i=1; i<point.coords.length; i++)
      {
        double coord = point.coords[ i];
        if ( coord < min[ i]) min[ i] = coord;
        if ( coord > max[ i]) max[ i] = coord;
      }
    }
  }

  /**
   * Find the index of the specified point.
   * @param point The point.
   * @return Returns -1 or the index.
   */
  public int indexOf( Point point)
  {
    return points.indexOf( point);
  }
  
  /**
   * @return Returns the minimum boundary of the plot.
   */
  public double[] getMinimum()
  {
    return min;
  }
  
  /**
   * @return Returns the maximum boundary of the plot.
   */
  public double[] getMaximum()
  {
    return max;
  }
  
  /**
   * @return Returns the foreground color.
   */
  public String getForeground()
  {
    return foreground;
  }
  
  /**
   * @return Returns the background color.
   */
  public String getBackground()
  {
    return background;
  }
  
  /**
   * Returns true if any coordinate of the specified point lies on the boundary.
   * @param point The point.
   * @return Returns true if any coordinate of the specified point lies on the boundary.
   */
  private boolean isExtreme( Point point)
  {
    for( int i=0; i<point.coords.length; i++)
    {
      double coord = point.coords[ i];
      if ( coord == min[ i] || coord == max[ i]) return true;
    }
    return false;
  }
  
  private List<Point> points;
  private String foreground;
  private String background;
  private double[] min;
  private double[] max;
}
