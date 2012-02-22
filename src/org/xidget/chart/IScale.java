/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

import java.util.List;

/**
 * An interface for calculating tick marks on a scale.  The interface supports multiple
 * levels of tick marks.  It provides methods for transforming values to and from the numeric
 * space defined by the ticks.
 */
public interface IScale
{
  public static class Tick
  {
    public int depth;
    public double value;
    public double scale; 
    public String label;
  }
  
  /**
   * @return Returns the tick marks.
   */
  public List<Tick> getTicks();

  /**
   * @return Returns the tick counts by depth.
   */
  public List<Integer> getTickCounts();

  /**
   * Returns the location of the specified value relative to the scale where
   * a value of 0 is the minimum tick value and a value of 1 is the maximum
   * tick value.  
   * @param value The value to be converted.
   * @return Returns the location of the specified value in the scale space.
   */
  public double plot( double value);

  /**
   * Returns the value of the specified position on the scale. This method is
   * the inverse of the plot( double) method.
   * @param scale A value between 0 and 1, inclusive.
   * @return Returns the value of the specified position on the scale.
   */
  public double value( double scale);

  /**
   * Interpolates the value represented by the specified index on a grid of 
   * size equally spaced points.  This method compensates for aliasing that
   * is caused by rounding tick values to the grid.  
   * @param index A value between 0 (inclusive), and size (exclusive).
   * @param size The size of the grid.
   * @return Returns the nearest dealiased/interpolated value.
   */
  public double value( int index, int size);
}