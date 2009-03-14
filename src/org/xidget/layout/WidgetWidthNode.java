/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetFeature.Bounds;

/**
 * An anchor which represents the width of a container.
 */
public class WidgetWidthNode extends ComputeNode
{
  /**
   * Create an anchor valueed relative to its container widget.
   * @param container The widget.
   */
  public WidgetWidthNode( IWidgetFeature container)
  {
    this.container = container;
    this.bounds = new Bounds();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    return container.hasBounds();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public int getValue()
  {
    container.getBounds( bounds);
    return bounds.width;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    container.getBounds( bounds);
    container.setBounds( bounds.x, bounds.y, value, bounds.height);
  }

  private IWidgetFeature container;
  private Bounds bounds;
}
