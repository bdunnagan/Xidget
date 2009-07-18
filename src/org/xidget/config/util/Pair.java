/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.util;

/**
 * An object which represents a pair of coordinates in a xidget configuration.
 */
public class Pair
{
  public Pair( int x, int y)
  {
    this.x = x;
    this.y = y;
  }
  
  public Pair( String string)
  {
    this( string, 0, 0);
  }
  
  /**
   * Parse the pair and use the specified defaults if the string is null or empty.
   * @param string The string.
   * @param defaultX The default value for x.
   * @param defaultY The default value for y. 
   */
  public Pair( String string, int defaultX, int defaultY)
  {
    if ( string == null || string.length() == 0)
    {
      x = defaultX;
      y = defaultY;
      return;
    }
    
    String[] parts = string.split( "\\s*,\\s*");
    x = Integer.parseInt( parts[ 0].trim());
    y = Integer.parseInt( parts[ 1].trim());    
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%d, %d", x, y);
  }

  public int x;
  public int y;
}
