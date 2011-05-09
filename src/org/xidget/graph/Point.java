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
  public enum Style 
  { 
    circleBig, 
    squareBig, 
    triangleBig, 
    diamondBig, 
    plusBig, 
    exBig,
    squareSmall, 
    triangleSmall, 
    diamondSmall, 
    plusSmall, 
    exSmall,
    dot
  };
  
  public Point()
  {
    style = Style.circleBig;
  }
  
  public double[] coords;
  public Style style;
  public String color;
  public String label;
  
  public Point prev;
  public Point next;
}
