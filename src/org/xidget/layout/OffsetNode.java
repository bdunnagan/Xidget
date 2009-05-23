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
  public float getValue()
  {
    return value;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
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
    sb.append( "offset( "); sb.append( offset);
    for( IComputeNode dependency: getDependencies())
    {
      sb.append( ", ");
      sb.append( dependency);
    }
    sb.append( ")");
    return sb.toString();
  }

  private int offset;
  private float value;
}
