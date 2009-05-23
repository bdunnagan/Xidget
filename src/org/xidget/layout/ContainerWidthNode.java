/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;

/**
 * An anchor which represents the width of a container.
 */
public class ContainerWidthNode extends ComputeNode
{
  /**
   * Create an anchor valueed relative to its container widget.
   * @param container The widget.
   */
  public ContainerWidthNode( IWidgetFeature container)
  {
    this.container = container;
    this.bounds = new Bounds();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#hasValue()
   */
  @Override
  public boolean hasValue()
  {
    return true;
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

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( container); sb.append( ":CONTAINER_WIDTH");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetFeature container;
  private Bounds bounds;
}
