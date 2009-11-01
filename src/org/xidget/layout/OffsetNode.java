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
    this( "", node, offset);
  }
  
  public OffsetNode( String name, IComputeNode node, int offset)
  {
    this.name = name;
    this.offset = offset;
    addDependency( node);
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
    return String.format( "%d. %s (%s) %+d <- %s", getID(), name, printValue(), offset, printDependencies());
  }
  
  private String name;
  private int offset;
}
