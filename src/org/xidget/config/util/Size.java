/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.util;

/**
 * An object which represents a size definition in a xidget configuration.
 */
public class Size
{
  public Size( String string)
  {
    this( string, 0, 0);
  }
  
  /**
   * Parse the size and use the specified defaults if the string is null or empty.
   * @param string The string.
   * @param defaultX The default x-coordinate to use when the string is null or empty.
   * @param defaultY The default y-coordinate to use when the string is null or empty. 
   */
  public Size( String string, int defaultX, int defaultY)
  {
    if ( string == null || string.length() == 0)
    {
      x = defaultX;
      y = defaultY;
      return;
    }
    
    String[] parts = string.split( "\\s*[x]\\s*");
    x = Integer.parseInt( parts[ 0].trim());
    y = Integer.parseInt( parts[ 1].trim());    
  }
  
  public int x;
  public int y;
}
