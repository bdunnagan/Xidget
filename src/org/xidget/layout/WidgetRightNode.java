/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetFeature.Bounds;

/**
 * An anchor which represents the right side of widget.
 */
public class WidgetRightNode extends ComputeNode
{
  /**
   * Create the anchor.
   * @param widget The widget.
   */
  public WidgetRightNode( IWidgetFeature widget)
  {
    this.widget = widget;
    this.bounds = new Bounds();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    return widget.hasBounds();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public int getValue()
  {
    widget.getBounds( bounds);
    return bounds.x + bounds.width;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    widget.getBounds( bounds);
    widget.setBounds( bounds.x, bounds.y, value - bounds.x, bounds.height);
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
