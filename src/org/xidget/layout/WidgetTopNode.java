/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;
import org.xmodel.util.Radix;

/**
 * An anchor which represents the top of a widget.
 */
public class WidgetTopNode extends ComputeNode
{
  /**
   * Create the anchor.
   * @param widget The widget.
   */
  public WidgetTopNode( IWidgetFeature widget)
  {
    this.widget = widget;
    this.bounds = new Bounds();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public int getValue()
  {
    widget.getBounds( bounds);
    return bounds.y;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    widget.getBounds( bounds);
    widget.setBounds( bounds.x, value, bounds.width, bounds.height);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "@"); sb.append( Radix.convert( widget.hashCode(), 36)); sb.append( ".y0");
    return sb.toString();
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
