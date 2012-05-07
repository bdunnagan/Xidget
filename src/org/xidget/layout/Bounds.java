/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Bounds.java
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
public final class Bounds
{
  public Bounds()
  {
    this.x = undefined;
    this.y = undefined;
    this.width = undefined;
    this.height = undefined;
  }
  
  public Bounds( Bounds bounds)
  {
    this.x = bounds.x;
    this.y = bounds.y;
    this.width = bounds.width;
    this.height = bounds.height;
  }
  
  public Bounds( float x, float y, float width, float height)
  {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
  
  /**
   * @return Returns true if the specified coordinate is defined.
   */
  public boolean isXDefined()
  {
    return x != undefined;
  }
  
  /**
   * @return Returns true if the specified coordinate is defined.
   */
  public boolean isYDefined()
  {
    return y != undefined;
  }
  
  /**
   * @return Returns true if the specified coordinate is defined.
   */
  public boolean isWidthDefined()
  {
    return width != undefined;
  }
  
  /**
   * @return Returns true if the specified coordinate is defined.
   */
  public boolean isHeightDefined()
  {
    return height != undefined;
  }
  
  /**
   * Parse the bounds from the specified string. Any bounds value which is less than zero
   * will be ignored and the corresponding bounds parameter will be left unchanged.
   * @param string The bounds string.
   * @return Returns true if the parse was successful.
   */
  public boolean parse( String string)
  {
    try
    {
      if ( string == null) return false;

      String[] parts = string.split( "\\s*+,\\s*+");
      if ( parts.length == 2)
      {
        width = parts[ 0].equals( "?")? undefined: Float.parseFloat( parts[ 0]);
        height = parts[ 1].equals( "?")? undefined: Float.parseFloat( parts[ 1]);
        return true;
      }
      else if ( parts.length == 4)
      {
        x = parts[ 0].equals( "?")? undefined: Float.parseFloat( parts[ 0]);
        y = parts[ 1].equals( "?")? undefined: Float.parseFloat( parts[ 1]);
        width = parts[ 2].equals( "?")? undefined: Float.parseFloat( parts[ 2]);
        height = parts[ 3].equals( "?")? undefined: Float.parseFloat( parts[ 3]);
        return true;
      }
    }
    catch( Exception e)
    {
    }
    
    return false;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "%.0f, %.0f, %.0f, %.0f", x, y, width, height);
  }
  
  private final static float undefined = Float.POSITIVE_INFINITY;
  
  public float x;
  public float y;
  public float width;
  public float height;
}
