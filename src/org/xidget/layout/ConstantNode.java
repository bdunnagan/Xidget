/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An IComputeNode with a constant value.
 */
public class ConstantNode extends ComputeNode
{
  public ConstantNode( int value)
  {
    super();
    setValue( value);
  }
  
  public ConstantNode( float value)
  {
    super();
    setValue( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#reset()
   */
  @Override
  public void reset()
  {
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%d. (%s) <- %s", getID(), printValue(), printDependencies());
  }
}
