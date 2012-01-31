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
   * Parse a color specification.
   * @param color The color specification.
   * @return Returns an array of 3 or 4 floats containing the red, green, blue and optional alpha channels.
   */
  public static float[] parseColor( String color)
  {
    float[] result = null;
    
    int length = color.length();
    switch( length)
    {
      case 3:
      {
        result = new float[ 3];
        result[ 0] = Integer.parseInt( color.substring( 0, 1), 16) / 16f;
        result[ 1] = Integer.parseInt( color.substring( 1, 2), 16) / 16f;
        result[ 2] = Integer.parseInt( color.substring( 2), 16) / 16f;
      }
      break;
        
      case 4:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 1), 16) / 16f;
        result[ 1] = Integer.parseInt( color.substring( 1, 2), 16) / 16f;
        result[ 2] = Integer.parseInt( color.substring( 2, 3), 16) / 16f;
        result[ 3] = Integer.parseInt( color.substring( 3), 16) / 16f;
      }
      break;
        
      case 6:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 2), 16) / 256f;
        result[ 1] = Integer.parseInt( color.substring( 2, 4), 16) / 256f;
        result[ 2] = Integer.parseInt( color.substring( 4), 16) / 256f;
      }
      break;
        
      case 8:
      {
        result = new float[ 4];
        result[ 0] = Integer.parseInt( color.substring( 0, 2), 16) / 256f;
        result[ 1] = Integer.parseInt( color.substring( 2, 4), 16) / 256f;
        result[ 2] = Integer.parseInt( color.substring( 4, 6), 16) / 256f;
        result[ 3] = Integer.parseInt( color.substring( 6), 16) / 256f;
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
