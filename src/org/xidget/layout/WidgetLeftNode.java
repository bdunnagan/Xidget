/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetFeature.Bounds;
import org.xmodel.util.Radix;

/**
 * An anchor which represents the left side of a widget.
 */
public class WidgetLeftNode extends ComputeNode
{
  /**
   * Create the anchor.
   * @param widget The widget.
   */
  public WidgetLeftNode( IWidgetFeature widget)
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
    return bounds.x;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    widget.getBounds( bounds);
    widget.setBounds( value, bounds.y, bounds.width, bounds.height);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "@"); sb.append( Radix.convert( widget.hashCode(), 36)); sb.append( ".x0");
    return sb.toString();
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
