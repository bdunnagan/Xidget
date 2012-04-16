/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.shape;

/**
 * An interface for setting parameters of a shape xidget.
 */
public interface IShapeFeature
{
  /**
   * Specify whether the shape should be filled.
   * @param fill True if the shape should be filled.
   */
  public void setFill( boolean fill);
  
  /**
   * Set the stroke width.
   * @param width The width in pixels.
   */
  public void setStrokeWidth( double width);
  
  /**
   * Specifies whether the coordinates are specified as a percentage of the canvas size.
   * @param relative True if relative coordinates should be used.
   */
  public void setRelativeCoordinates( boolean relative);
}
