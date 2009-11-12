/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Margins.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.layout;

/**
 * A structure containing the margins of the inside of a container.
 */
public class Margins
{
  public Margins()
  {
  }
  
  public Margins( int x0, int y0, int x1, int y1)
  {
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
  }
  
  public Margins( String string)
  {
    parse( string);
  }
  
  /**
   * Parse the specified string into margins using the specified default values if parsing fails.
   * @param string The string.
   * @param x0 The default x0.
   * @param y0 The default y0.
   * @param x1 The default x1.
   * @param y1 The default y1.
   */
  public Margins( String string, int x0, int y0, int x1, int y1)
  {
    if ( !parse( string))
    {
      this.x0 = x0;
      this.y0 = y0;
      this.x1 = x1;
      this.y1 = y1;
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%d, %d, %d, %d", x0, y0, x1, y1);
  }
  
  /**
   * Parse margins from the specified string.
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
      if ( end < 0) 
      {
        x0 = y0 = x1 = y1 = Integer.parseInt( string);
      }
      else
      {
        x0 = Integer.parseInt( string.substring( start, end).trim());
        
        start = end + 1; end = string.indexOf( ',');
        y0 = Integer.parseInt( string.substring( start, end).trim());
        
        start = end + 1; end = string.indexOf( ',');
        x1 = Integer.parseInt( string.substring( start, end).trim());
        
        start = end + 1;
        y1 = Integer.parseInt( string.substring( start).trim());
        
        return true;
      }
    }
    catch( Exception e)
    {
    }
    
    return false;
  }
  
  public int x0;
  public int y0;
  public int x1;
  public int y1;
}
