/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;

/**
 * An anchor which represents the vertical center of a widget.
 */
public class WidgetHorizontalCenterNode extends ComputeNode
{
  /**
   * Create the anchor.
   * @param widget The widget.
   */
  public WidgetHorizontalCenterNode( IWidgetFeature widget)
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
    return bounds.x + bounds.width / 2;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
  {
    widget.getBounds( bounds);
    widget.setBounds( value - (bounds.width / 2), bounds.y, bounds.width, bounds.height);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( widget); sb.append( ":HORIZ_CENTER");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetFeature widget;
  private Bounds bounds;
}
