/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.canvas;

import org.xidget.IXidget;

/**
 * An interface for xidgets that provide a canvas on which other xidgets can paint themselves. Xidgets
 * that paint themselves export an IPaintFeature. This is the simple basis on which more complex custom
 * graphics is implemented.
 */
public interface ICanvasFeature
{
  /**
   * Add a layer to the canvas. 
   * @param layer The name of the layer.
   * @param zorder The z-order of the layer (lesser values are painted first).
   */
  public void addLayer( String layer, int zorder);
  
  /**
   * Remove a layer from the canvas.
   * @param layer The name of the layer.
   */
  public void removeLayer( String layer);
  
  /**
   * Add the specified xidget to the canvas.
   * @param child The xidget.
   */
  public void addChild( IXidget child);
  
  /**
   * Remove the specified xidget from the canvas.
   * @param child The xidget.
   */
  public void removeChild( IXidget child);
  
  /**
   * Returns the width of the canvas.
   * @return Returns the width of the canvas.
   */
  public int getWidth();
  
  /**
   * Returns the height of the canvas.
   * @return Returns the height of the canvas.
   */
  public int getHeight();
  
  /**
   * Tell the canvas to repaint itself.
   */
  public void repaint();
  
  /**
   * Tell the canvas to repaint the specified rectangle.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   * @param width The width of the rectangle.
   * @param height The height of the rectangle.
   */
  public void repaint( int x, int y, int width, int height);
}
