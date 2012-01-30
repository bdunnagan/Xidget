/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

/**
 * A class that stores a point of arbitrary dimension. This class provides
 * references to the previous and next points in a linked-list to support
 * line drawing.
 */
public class Point
{
  public double[] coords;
  public String[] coordLabels;
  public String label;
  public String foreground;
  public String background;
}
