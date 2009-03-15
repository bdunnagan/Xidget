/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An IComputeNode which is a fixed offset from another node.
 */
public class OffsetNode extends ComputeNode
{
  public OffsetNode( IComputeNode node, int offset)
  {
    this.node = node;
    this.offset = offset;
    addDependency( node);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    return node.hasValue();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public int getValue()
  {
    return node.getValue() + offset;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    node.setValue( value - offset);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#update()
   */
  @Override
  public void update()
  {
  }

  private IComputeNode node;
  private int offset;
}
