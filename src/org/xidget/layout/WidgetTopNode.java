/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetFeature.Bounds;

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

  private IWidgetFeature widget;
  private Bounds bounds;
}
