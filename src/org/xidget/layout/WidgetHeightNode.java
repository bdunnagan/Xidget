/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetFeature.Bounds;

/**
 * An anchor which is valueed relative to the height of a container widget.
 */
public class WidgetHeightNode extends ComputeNode
{
  /**
   * Create an anchor valueed relative to the height of a container widget.
   * @param container The widget.
   */
  public WidgetHeightNode( IWidgetFeature container)
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
    return bounds.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    container.getBounds( bounds);
    container.setBounds( bounds.x, bounds.y, bounds.width, value);
  }

  private IWidgetFeature container;
  private Bounds bounds;
}
