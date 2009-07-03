/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;

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
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    widget.getBounds( bounds);
    return bounds.x + bounds.width;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
  {
    widget.getBounds( bounds);
    widget.setBounds( bounds.x, bounds.y, value - bounds.x, bounds.height);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( widget); sb.append( ":RIGHT");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
