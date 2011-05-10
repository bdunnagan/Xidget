/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.graph;

/**
 * A class that defines shapes for the various point styles. The shapes are
 * defined as a series of line segments.
 */
public class PointStyles
{
  /**
   * Returns the line segments for the specified point style.
   * @param style The point style.
   * @return Returns the line segments for the specified point style.
   */
  public final static int[][] getLineSegments( Point.Style style)
  {
    switch( style)
    {
      case circle:     return circle5x5; 
      case square:     return square5x5;
      case triangle:   return triangle7x7;
      case diamond:    return diamond7x7;
      case plus:       return plus7x7;
      case ex:         return ex7x7;
    }
    return null;
  }
  
  private final static int[][] circle5x5 = 
  {
    {-1, -2}, {1, -2}, {1, -2}, {2, -1}, {2, -1}, {2, 1},
    {2, 1}, {1, 2}, {1, 2}, {-1, 2}, {-1, 2}, {-2, 1}, 
    {-2, 1}, {-2, -1}, {-2, -1}, {-1, -2}
  };
  
  private final static int[][] square5x5 = 
  {
    {-2, -2}, {2, -2}, {2, -2}, {2, 2}, 
    {2, 2}, {-2, 2}, {-2, 2}, {-2, -2}
  };
  
  private final static int[][] triangle7x7 = 
  {
    {0, -2}, {2, 2}, {2, 2}, {-2, 2}, {-2, 2}, {0, -2}
  };
  
  private final static int[][] diamond7x7 = 
  {
    {0, -2}, {2, 0}, {2, 0}, {0, 2}, 
    {0, 2}, {-2, 0}, {-2, 0}, {0, -2}  
  };
  
  private final static int[][] plus7x7 = 
  {
    {0, -2}, {0, 2}, {-2, 0}, {2, 0}
  };
    
  private final static int[][] ex7x7 = 
  {
    {-2, -2}, {2, 2}, {-2, 2}, {2, -2}  
  };
}
