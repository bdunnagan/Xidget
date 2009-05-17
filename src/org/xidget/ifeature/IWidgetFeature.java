/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for all widgets used by IXidget implementations. Instances of this interface
 * and its sub-interfaces are the contract by which IXidget implementations manipulate their
 * associated widgets.
 */
public interface IWidgetFeature
{
  public class Bounds
  {
    public int x;
    public int y;
    public int width;
    public int height;
  }
  
  /**
   * Set the bounds of the widget.
   * @param x The left-side x-coordinate.
   * @param y The top-side y-coordinate.
   * @param width The width of the widget.
   * @param height The height of the widget.
   */
  public void setBounds( int x, int y, int width, int height);
  
  /**
   * Returns the bounds of the widget.
   * @param result The result.
   */
  public void getBounds( Bounds result);
  
  /**
   * Set whether the widget is visible.
   * @param visible True if the widget is visible.
   */
  public void setVisible( boolean visible);
  
  /**
   * Set whether the widget is enabled.
   * @param enabled True if enabled.
   */
  public void setEnabled( boolean enabled);
  
  /**
   * Set the tooltip of the widget.
   * @param tooltip The tooltip.
   */
  public void setTooltip( String tooltip);
}
