/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for converting color specifications into platform color objects of the specified type.
 */
public interface IColorFeature<U, V>
{
  /**
   * Convert the specified color specification into a platform-specific object.
   * @param color The color specification.
   * @return Returns the platform-specific Color object.
   */
  public U getColor( Object color);
  
  /**
   * Apply the specified color specification to the specified platform-specific graphics context. If the 
   * color specification represents a gradient, then the gradient is calculated using the width and height 
   * arguments. 
   * @param color The color specification.
   * @param graphics The platform-specific graphics context.
   * @param width The width of the gradient, if present.
   * @param height The height of the gradient, if present.
   */
  public void applyColor( Object color, V graphics, int width, int height);
}
