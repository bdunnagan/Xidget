/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;

/**
 * An anchor which is valued relative to the height of a container widget.
 */
public class WidgetHeightNode extends ComputeNode
{
  /**
   * Create an anchor valued relative to the height of a container widget.
   * @param container The widget.
   */
  public WidgetHeightNode( IWidgetFeature container)
  {
    this.container = container;
    this.bounds = new Bounds();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    container.getBounds( bounds);
    return bounds.height;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
  {
    container.getBounds( bounds);
    container.setBounds( bounds.x, bounds.y, bounds.width, value);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( container); sb.append( ":HEIGHT");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetFeature container;
  private Bounds bounds;
}
