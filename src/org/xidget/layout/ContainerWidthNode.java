/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetContainerFeature;

/**
 * An anchor which is valued relative to the height of a container widget.
 */
public class ContainerWidthNode extends ComputeNode
{
  /**
   * Create an anchor valued relative to the height of a container widget.
   * @param container The widget.
   */
  public ContainerWidthNode( IWidgetContainerFeature container)
  {
    this.container = container;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    return container.getWidth();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
  {
    container.setWidth( (int)Math.round( value));
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( container); sb.append( ":C_WIDTH");
    sb.append( printDependencies());
    return sb.toString();
  }

  private IWidgetContainerFeature container;
}
