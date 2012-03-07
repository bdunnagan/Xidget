/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeWidgetFeature;

/**
 * Xidget utilities.
 */
public class XidgetUtil
{
  /**
   * Find the root of a tree xidget hierarchy. If the xidget is not a tree then return the argument.
   * @param xidget A tree or sub-tree xidget.
   * @return Returns the argument or the root of the tree xidget hierarchy.
   */
  public static IXidget findTreeRoot( IXidget xidget)
  {
    IXidget parent = xidget.getParent();
    while( parent != null)
    {
      ITreeWidgetFeature feature = parent.getFeature( ITreeWidgetFeature.class);
      if ( feature == null) return xidget;
      xidget = parent;
      parent = parent.getParent();
    }
    return xidget;
  }
  
  /**
   * Parse an 8-bit per channel color specification string in one of 8 formats (Y means gray-scale): 
   * <ul>
   * <li>1 char Y</li>
   * <li>2 char YY</li>
   * <li>3 char RGB</li>
   * <li>4 char RGBA</li>
   * <li>5 char RGBAA</li>
   * <li>6 char RRGGBB</li>
   * <li>7 char RRGGBBA</li>
   * <li>8 char RRGGBBAA</li>
   * </ul>
   * The following characters are removed from the string before parsing, ' ', ':', '/'.
   * @param color The color specification.
   * @return Returns an array of 3 or 4 floats containing the red, green, blue and optional alpha channels.
   */
  public static float[] parseColor( String color)
  {
    color = color.replaceAll( "[ :/]++", "");  
    
    float[] result = null;
    int length = color.length();
    switch( length)
    {
      case 1:
      {
        result = new float[ 3];
        result[ 0] = result[ 1] = result[ 2] = Integer.parseInt( color, 16) / 15f;
      }
      break;
        
      case 2:
      {
        result = new float[ 3];
        result[ 0] = result[ 1] = result[ 2] = Integer.parseInt( color, 16) / 255f;
      }
      break;
        
      case 3:
      {
        result = new float[ 3];
        result[ 0] = Integer.parseInt( color.substring( 0, 1), 16) / 15f;
        result[ 1] = Integer.parseInt( color.substring( 1, 2), 16) / 15f;
        result[ 2] = Integer.parseInt( color.substring( 2), 16) / 15f;
      }
      break;
        
      case 4:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 1), 16) / 15f;
        result[ 1] = Integer.parseInt( color.substring( 1, 2), 16) / 15f;
        result[ 2] = Integer.parseInt( color.substring( 2, 3), 16) / 15f;
        result[ 3] = Integer.parseInt( color.substring( 3), 16) / 15f;
      }
      break;
        
      case 5:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 1), 16) / 15f;
        result[ 1] = Integer.parseInt( color.substring( 1, 2), 16) / 15f;
        result[ 2] = Integer.parseInt( color.substring( 2, 3), 16) / 15f;
        result[ 3] = Integer.parseInt( color.substring( 3), 16) / 255f;
      }
      break;
        
      case 6:
      {
        result = new float[ 3];
        result[ 0] = Integer.parseInt( color.substring( 0, 2), 16) / 255f;
        result[ 1] = Integer.parseInt( color.substring( 2, 4), 16) / 255f;
        result[ 2] = Integer.parseInt( color.substring( 4), 16) / 255f;
      }
      break;
        
      case 7:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 2), 16) / 255f;
        result[ 1] = Integer.parseInt( color.substring( 2, 4), 16) / 255f;
        result[ 2] = Integer.parseInt( color.substring( 4, 6), 16) / 255f;
        result[ 3] = Integer.parseInt( color.substring( 6), 16) / 15f;
      }
      break;
        
      case 8:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 2), 16) / 255f;
        result[ 1] = Integer.parseInt( color.substring( 2, 4), 16) / 255f;
        result[ 2] = Integer.parseInt( color.substring( 4, 6), 16) / 255f;
        result[ 3] = Integer.parseInt( color.substring( 6), 16) / 255f;
      }
      break;
    }

    if ( result == null)
    {
      throw new IllegalArgumentException( String.format(
        "Illegal color specification, '%s'.", color));
    }
    
    return result;
  }
}
