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
  public float getValue()
  {
    return value;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(int)
   */
  public void setValue( float value)
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
    sb.append( "proportional( ");
    sb.append( "%"); sb.append( percent); sb.append( ", ");
    sb.append( offset);
    for( IComputeNode dependency: getDependencies())
    {
      sb.append( ", ");
      sb.append( dependency);
    }
    sb.append( ")");
    return sb.toString();
  }

  private float percent;
  private int offset;
  private int value;
}
