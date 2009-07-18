/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * A structure containing the margins of the inside of a container.
 */
public class Bounds
{
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%2.1f, %2.1f, %2.1f, %2.1f", x, y, width, height);
  }
  
  public float x;
  public float y;
  public float width;
  public float height;
}
