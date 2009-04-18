/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.IWidgetFeature.Bounds;
import org.xmodel.util.Radix;

/**
 * An anchor which represents the width of a container.
 */
public class WidgetWidthNode extends ComputeNode
{
  /**
   * Create an anchor valueed relative to its container widget.
   * @param container The widget.
   * @param x0 The x0 anchor.
   * @param x1 The x1 anchor.
   */
  public WidgetWidthNode( IWidgetFeature container, IComputeNode x0, IComputeNode x1)
  {
    this.container = container;
    this.bounds = new Bounds();
    addDependency( x0);
    addDependency( x1);
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
    sb.append( "@"); sb.append( Radix.convert( container.hashCode(), 36)); sb.append( ".w");
    return sb.toString();
  }

  private IWidgetFeature container;
  private Bounds bounds;
}
