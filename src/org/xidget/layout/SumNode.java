/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.List;
import org.xidget.Log;

/**
 * An implementation of IComputeNode that computes the sum of two other nodes.
 */
public class SumNode extends ComputeNode
{
  /**
   * Compute lhs plus rhs.
   * @param lhs The left-hand-side.
   * @param rhs The right-hand-side.
   */
  public SumNode( IComputeNode lhs, IComputeNode rhs)
  {
    super.addDependency( lhs);
    super.addDependency( rhs);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#addDependency(org.xidget.layout.IComputeNode)
   */
  @Override
  public void addDependency( IComputeNode node)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#clearDependencies()
   */
  @Override
  public void clearDependencies()
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#update()
   */
  @Override
  public void update()
  {
    List<IComputeNode> nodes = getDependencies();
    if ( nodes.get( 0).hasValue() && nodes.get( 1).hasValue())
      setValue( nodes.get( 0).getValue() + nodes.get( 1).getValue());
    
    Log.printf( "layout", "update: %s\n", toString()); 
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%d. (%s) add%s", getID(), printValue(), printDependencies());
  }
}
