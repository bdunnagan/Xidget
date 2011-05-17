/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.graph;

/**
 * A class that stores a point of arbitrary dimension. This class provides
 * references to the previous and next points in a linked-list to support
 * line drawing.
 */
public class Point
{
  public Point()
  {
    shape = PointStyles.getLineSegments( PointStyles.Style.ex);
  }
  
  public double[] coords;
  public int[][] shape;
  public String color;
  public String label;
  
  public Point prev;
  public Point next;
}
