/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for converting color specifications into platform color objects of the specified type.
 */
public interface IColorFeature<T>
{
  /**
   * Convert the specified color specification into a platform-specific object.
   * @param color The color specification.
   * @return Returns the platform-specific Color object.
   */
  public T getColor( Object color);
}
