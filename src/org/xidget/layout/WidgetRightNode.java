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
   * @see org.xidget.layout.ComputeNode#hasValue()
   */
  @Override
  public boolean hasValue()
  {
    if ( super.hasValue()) return true;
    
    //
    // The right edge of a widget is always defined if its width is greater than zero.
    // The converse is not true, however.  The value may still be defined if the width
    // is zero in the case that the layout process computed a zero value for the width.
    //
    widget.getBounds( bounds);
    return ( bounds.width > 0);
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
