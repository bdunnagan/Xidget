/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * The size of a widget.
 */
public class Size
{
  public Size()
  {
  }
  
  public Size( int width, int height)
  {
    this.width = width;
    this.height = height;
  }
  
  public Size( String string)
  {
    parse( string);
  }
  
  /**
   * Parse the specified string. Use the specified width and height if parsing fails.
   * @param string The string.
   * @param width The default width.
   * @param height The default height.
   */
  public Size( String string, int width, int height)
  {
    if ( !parse( string))
    {
      this.width = width;
      this.height = height;
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%d, %d", width, height);
  }

  /**
   * Parse size from the specified string.
   * @param string The string.
   * @return Returns true if the parse was successful.
   */
  public boolean parse( String string)
  {
    try
    {
      if ( string == null) return false;
      
      string = string.trim();
      if ( string.length() == 0) return false;
      
      int start = 0;
      int end = string.indexOf( ',');
      if ( end > 0)
      {
        width = Integer.parseInt( string.substring( start, end).trim());
        
        start = end + 1;
        height = Integer.parseInt( string.substring( start).trim());
        
        return true;
      }
    }
    catch( Exception e)
    {
    }
    
    return false;
  }
  
  public int width;
  public int height;
}
