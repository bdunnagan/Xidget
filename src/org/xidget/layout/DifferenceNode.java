/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.List;
import org.xidget.Log;

/**
 * An implementation of IComputeNode that calculates the difference between two nodes.
 */
public class DifferenceNode extends ComputeNode
{
  /**
   * Computes node2 minus node1.
   * @param node1 The first node.
   * @param node2 The second node.
   */
  public DifferenceNode( IComputeNode node1, IComputeNode node2)
  {
    addDependency( node1);
    addDependency( node2);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    List<IComputeNode> nodes = getDependencies();
    return nodes.get( 0).getValue() - nodes.get( 1).getValue();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(float)
   */
  public void setValue( float value)
  {
    List<IComputeNode> nodes = getDependencies();
    nodes.get( 0).setValue( nodes.get( 1).getValue() + value);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    List<IComputeNode> nodes = getDependencies();
    return nodes.get( 0).hasValue() && nodes.get( 1).hasValue();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    if ( hasValue()) Log.printf( "layout", "update: (%.1f) %s\n", getValue(), toString()); 
    else Log.printf( "layout", "update: (?) %s\n", toString());
  }
}
