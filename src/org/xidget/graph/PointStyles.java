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
      case circleBig:     return circle7x7; 
      case squareBig:     return square7x7;
      case triangleBig:   return triangle7x7;
      case diamondBig:    return diamond7x7;
      case plusBig:       return plus7x7;
      case exBig:         return ex7x7;
      case squareSmall:   return square3x3;  
      case triangleSmall: return triangle3x3;
      case diamondSmall:  return diamond3x3;
      case plusSmall:     return plus3x3;
      case exSmall:       return ex3x3;
    }
    return null;
  }
  
  private final static int[][] circle7x7 = 
  {
    {2, 0}, {4, 0}, {4, 0}, {6, 2}, {6, 2}, {6, 4},
    {6, 4}, {4, 6}, {4, 6}, {2, 6}, {2, 6}, {0, 4}, 
    {0, 4}, {0, 2}, {0, 2}, {2, 0}
  };
  
  private final static int[][] square7x7 = 
  {
    {0, 0}, {6, 0}, {6, 0}, {6, 6}, 
    {6, 6}, {0, 6}, {0, 6}, {0, 0}
  };
  
  private final static int[][] triangle7x7 = 
  {
    {3, 0}, {6, 6}, {6, 6}, {0, 6}, {0, 6}, {3, 0}
  };
  
  private final static int[][] diamond7x7 = 
  {
    {3, 0}, {6, 3}, {6, 3}, {3, 6}, 
    {3, 6}, {0, 3}, {0, 3}, {3, 0}  
  };
  
  private final static int[][] plus7x7 = 
  {
    {3, 0}, {3, 6}, {0, 3}, {6, 3}
  };
    
  private final static int[][] ex7x7 = 
  {
    {0, 0}, {6, 6}, {0, 6}, {6, 0}  
  };
  
  private final static int[][] square3x3 = 
  { 
    {0, 0}, {2, 0}, {2, 0}, {2, 2}, 
    {2, 2}, {0, 2}, {0, 2}, {0, 0}
  };
  
  private final static int[][] diamond3x3 = 
  { 
    {1, 0}, {2, 1}, {2, 1}, {1, 2}, 
    {1, 2}, {0, 1}, {0, 1}, {1, 0}  
  };
  
  private final static int[][] triangle3x3 = 
  {
    {1, 0}, {2, 2}, {2, 2}, {0, 2}, {0, 2}, {1, 0}
  };
  
  private final static int[][] plus3x3 = 
  {
    {1, 0}, {1, 2}, {0, 1}, {2, 1}
  };
    
  private final static int[][] ex3x3 = 
  {
    {0, 0}, {2, 2}, {0, 2}, {2, 0}  
  };
}
