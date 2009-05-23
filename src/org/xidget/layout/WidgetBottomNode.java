/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;

/**
 * An anchor which represents the bottom side of a widget.
 */
public class WidgetBottomNode extends ComputeNode
{
  /**
   * Create the anchor.
   * @param widget The widget.
   */
  public WidgetBottomNode( IWidgetFeature widget)
  {
    this.widget = widget;
    this.bounds = new Bounds();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#hasValue()
   */
  @Override
  public boolean hasValue()
  {
    if ( super.hasValue()) return true;
    
    //
    // The bottom edge of a widget is always defined if its height is greater than zero.
    // The converse is not true, however.  The value may still be defined if the height
    // is zero in the case that the layout process computed a zero value for the height.
    //
    widget.getBounds( bounds);
    return ( bounds.height > 0);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    widget.getBounds( bounds);
    return bounds.y + bounds.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
  {
    widget.getBounds( bounds);
    widget.setBounds( bounds.x, bounds.y, bounds.width, value - bounds.y);
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( widget); sb.append( ":BOTTOM");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
