/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Size.java
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
