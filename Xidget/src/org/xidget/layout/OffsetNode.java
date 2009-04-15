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
    this.offset = offset;
    addDependency( node);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public int getValue()
  {
    return value;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( int value)
  {
    this.value = value + offset;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if ( offset >= 0) sb.append( "+"); sb.append( offset);
    return sb.toString();
  }

  private int offset;
  private int value;
}
