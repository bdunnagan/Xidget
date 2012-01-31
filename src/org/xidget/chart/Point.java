/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.chart;

/**
 * A class that stores a point of arbitrary dimension along with display characteristics.  This class
 * defines detail listeners that should be installed on the nodes that represent points in a plot.  This
 * mechanism is designed around the following principles:
 * <ul>
 * <li>The implementation of IPlotFeature does not have to add its own listeners.</li>
 * <li>The implementation receives specific notifications that can be used to optimize repainting.</li>
 * <li>The model is simple to maximize painting performance.</li>
 * </ul>
 */
public class Point
{
  public Point( Plot plot)
  {
    this.plot = plot;
  }
  
  public Plot plot;
  public double[] coords;
  public String label;
  public String fcolor;
  public String bcolor;
}
