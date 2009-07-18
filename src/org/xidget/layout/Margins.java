/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * A structure containing the margins of the inside of a container.
 */
public class Margins
{
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%d, %d, %d, %d", x0, y0, x1, y1);
  }
  
  public int x0;
  public int y0;
  public int x1;
  public int y1;
}
