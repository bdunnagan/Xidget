/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An implementation of IComputeNode that functions as the handle for one side of a widget.
 * This handle may be the outside of any widget or the inside of a container.
 */
public class WidgetHandle extends ComputeNode
{
  /**
   * Create a new WidgeHandle with the specified offset.
   * @param name The name of the handle.
   * @param offset The offset from the side of the widget.
   */
  public WidgetHandle( String name, int offset)
  {
    this.name = name;
    this.offset = offset;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#setValue(float)
   */
  public void setValue( float value)
  {
    super.setValue( value + offset);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%d. (%s) handle( %s) %+d <- %s", getID(), printValue(), name, offset, printDependencies());
  }
  
  private String name;
  private int offset;
}
