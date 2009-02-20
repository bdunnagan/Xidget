/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.layout.IAnchor;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for widget adapters.
 */
public interface IXidget
{
  /**
   * Build the Xidget hierarchy.
   * @param parent The parent.
   */
  public void build( IXidget parent);
  
  /**
   * Bind the Xidget in the specified context.
   * @param context The context.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind the Xidget.
   */
  public void unbind();
  
  /**
   * Returns the anchor with the specified name (e.g. top, left, right, bottom).
   * The reason the argument is a string instead of an enum is that it is left
   * to the implementation to decide what types of anchors it supports.
   * @param name The name of the anchor.
   * @return Returns the anchor with the specified name.
   */
  public IAnchor getAnchor( String name);
  
  /**
   * Layout the widget relative to its parent.
   * @param x The relative x-coordinate.
   * @param y The relative y-coordinate.
   * @param width The width of the widget (-1 for auto).
   * @param height The height of the widget (-1 for auto).
   */
  public void constrain( int x, int y, int width, int height);
  
  /**
   * Returns the widget.
   * @return Returns the widget.
   */
  public Object getWidget();  
}
