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
    this.percent = percent;
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
    this.value = (int)Math.round( value * percent) + offset;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "%"); sb.append( percent);
    if ( offset >= 0) sb.append( "+"); sb.append( offset);
    return sb.toString();
  }

  private float percent;
  private int offset;
  private int value;
}
