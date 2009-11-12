/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.canvas;

/**
 * An interface for xidgets that paint themselves on their parent xidget. The parent xidget exports
 * an instance of ICanvasFeature.
 */
public interface IPaintFeature
{
  /**
   * Set the layer on which the associated xidget will be painted.
   * @param layer Null or the name of the layer.
   */
  public void setLayer( String layer);
  
  /**
   * Returns the name of the layer on which this xidget will be painted. If null is returned then
   * the xidget will be painted on the default layer. The z-order of the default layer is implementation
   * dependent.
   * @return Returns null or the name of the layer on which this xidget will be painted.
   */
  public String getLayer();
  
  /**
   * Paint the xidget to which this feature belongs using the specified graphics context.
   * @param graphics The graphics context.
   */
  public void paint( Object graphics); 
}
