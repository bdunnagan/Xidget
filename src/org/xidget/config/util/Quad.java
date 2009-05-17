/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.util;

/**
 * An object which represents a pair of coordinates in a xidget configuration.
 */
public class Quad
{
  public Quad( int a, int b, int c, int d)
  {
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
  }
  
  public Quad( String string)
  {
    this( string, 0, 0, 0, 0);
  }
  
  /**
   * Parse the pair and use the specified defaults if the string is null or empty.
   * @param string The string.
   * @param defaultA The default value for a.
   * @param defaultB The default value for b.
   * @param defaultC The default value for c.
   * @param defaultD The default value for d.
   */
  public Quad( String string, int defaultA, int defaultB, int defaultC, int defaultD)
  {
    if ( string == null || string.length() == 0)
    {
      a = defaultA;
      b = defaultB;
      c = defaultC;
      d = defaultD;
      return;
    }
    
    String[] parts = string.split( "\\s*,\\s*");
    a = Integer.parseInt( parts[ 0].trim());
    b = Integer.parseInt( parts[ 1].trim());
    c = Integer.parseInt( parts[ 1].trim());    
    d = Integer.parseInt( parts[ 1].trim());    
  }
  
  public int a;
  public int b;
  public int c;
  public int d;
}
