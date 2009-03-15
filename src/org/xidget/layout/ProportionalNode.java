/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An IComputeNode which has a proportional relationship to another node.
 */
public class ProportionalNode extends ComputeNode
{
  public ProportionalNode( IComputeNode node, float percent, int offset)
  {
    this.node = node;
    this.percent = percent;
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
    return (int)Math.round( node.getValue() * percent) + offset;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    node.setValue( (int)Math.round( (value - offset) / percent));
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#update()
   */
  @Override
  public void update()
  {
  }

  private IComputeNode node;
  private float percent;
  private int offset;
}
