/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.graph;

import java.util.List;

import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * A class that defines shapes for the various point styles. The shapes are
 * defined as a series of line segments.
 */
public class PointStyles
{
  public enum Style 
  { 
    circle, 
    square, 
    triangle, 
    diamond, 
    plus,
    dash,
    ex,
    bar,
    dot
  }
  
  /**
   * Returns the line segments defined in the specified element.
   * @param element The element.
   * @return Returns the line segments defined in the specified element.
   */
  public final static int[][] getLineSegments( IModelObject element)
  {
    List<IModelObject> children = element.getChildren();
    int[][] segments = new int[ children.size() * 2][];
    
    int[] curr = null;
    for( int i=0, j=0; i<children.size(); i++)
    {
      int[] prev = curr;
      
      IModelObject child = children.get( i);
      curr = parse( Xlate.get( child, "0, 0"));
      
      if ( child.isType( "move"))
      {
        if ( prev != null) 
        {
          segments[ j++] = prev;
          segments[ j++] = prev;
        }
      }
      else if ( child.isType( "line"))
      {
        if ( prev != null) 
        {
          segments[ j++] = prev;
          segments[ j++] = curr;
        }
      }
      else
      {
        throw new IllegalArgumentException( "Illegal shape specification.");
      }
    }
    
    return segments;
  }
  
  /**
   * Parse coordinates from the specified string.
   * @param string The string.
   * @return Returns the coordinates or null.
   */
  private static int[] parse( String string)
  {
    try
    {
      if ( string == null) return null;
      
      string = string.trim();
      if ( string.length() == 0) return null;
      
      int start = 0;
      int end = string.indexOf( ',');
      if ( end > 0)
      {
        int x = Integer.parseInt( string.substring( start, end).trim());
        
        start = end + 1;
        int y = Integer.parseInt( string.substring( start).trim());
        
        return new int[] { x, y};
      }
    }
    catch( Exception e)
    {
    }
    
    return null;
  }
  
  /**
   * Returns the line segments for the specified point style.
   * @param style The point style.
   * @return Returns the line segments for the specified point style.
   */
  public final static int[][] getLineSegments( Style style)
  {
    switch( style)
    {
      case circle:     return circle5x5; 
      case square:     return square5x5;
      case triangle:   return triangle5x5;
      case diamond:    return diamond5x5;
      case plus:       return plus5x5;
      case dash:       return dash5x5;
      case ex:         return ex5x5;
    }
    return null;
  }
  
  private final static int[][] circle5x5 = 
  {
    {-1, -2}, {1, -2}, {1, -2}, {2, -1}, {2, -1}, {2, 1},
    {2, 1}, {1, 2}, {1, 2}, {-1, 2}, {-1, 2}, {-2, 1}, 
    {-2, 1}, {-2, -1}, {-2, -1}, {-1, -2}
  };
  
  private final static int[][] square5x5 = 
  {
    {-2, -2}, {2, -2}, {2, -2}, {2, 2}, 
    {2, 2}, {-2, 2}, {-2, 2}, {-2, -2}
  };
  
  private final static int[][] triangle5x5 = 
  {
    {0, -2}, {2, 2}, {2, 2}, {-2, 2}, {-2, 2}, {0, -2}
  };
  
  private final static int[][] diamond5x5 = 
  {
    {0, -2}, {2, 0}, {2, 0}, {0, 2}, 
    {0, 2}, {-2, 0}, {-2, 0}, {0, -2}  
  };
  
  private final static int[][] plus5x5 = 
  {
    {0, -2}, {0, 2}, {-2, 0}, {2, 0}
  };
    
  private final static int[][] dash5x5 = 
  {
    {-2, 0}, {2, 0}
  };
  
  private final static int[][] ex5x5 = 
  {
    {-2, -2}, {2, 2}, {-2, 2}, {2, -2}  
  };
}
