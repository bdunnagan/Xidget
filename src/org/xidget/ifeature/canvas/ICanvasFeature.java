/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ICanvasFeature.java
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
