/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.ifeature.ILayoutFeature.Side;

/**
 * An implementation of IComputeNode that functions as an anchor within a container.
 */
public class AnchorNode extends ComputeNode
{
  /**
   * Create an AnchorNode that depends on the specified opposite sides of the inside 
   * of a container.  Note that the container nodes are not added to the dependencies
   * of this node because container nodes are always calculated first.
   * @param node1 The left or top container node.
   * @param node2 The right or bottom container node.
   * @param side The side identifying the dimension of the container.
   * @param fraction The fraction of the container width or height.
   * @param offset The offset from the fraction.
   */
  public AnchorNode( IComputeNode node1, IComputeNode node2, Side side, float fraction, int offset)
  {
    this.fraction = fraction;
    this.offset = offset;
    this.side = side;
  }
  
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

  /**
   * Set whether this anchor has a handle.
   * @param handle True if it has a handle.
   */
  public void setHandle( boolean handle)
  {
    this.handle = handle;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#hasXHandle()
   */
  @Override
  public boolean hasXHandle()
  {
    if ( side == Side.left || side == Side.right) return handle;
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#hasYHandle()
   */
  @Override
  public boolean hasYHandle()
  {
    if ( side == Side.top || side == Side.bottom) return handle;
    return false;
  }

  /**
   * Specify the fraction of the width or height of the container.
   * @param fraction The fractional value in the range [0, 1].
   */
  public void setFraction( float fraction)
  {
    this.fraction = fraction;
  }
  
  /**
   * Specify the offset from the position calculated by the fraction argument.
   * @param offset The offset.
   */
  public void setOffset( int offset)
  {
    this.offset = offset;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#update()
   */
  @Override
  public void update()
  {
    float a = node1.getValue();
    float b = node2.getValue();
    setValue( (b - a) * fraction + a + offset);
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%d. (%s) (%%%2.1f, %+d) <- %s", getID(), printValue(), fraction, offset, printDependencies());
  }

  private IComputeNode node1;
  private IComputeNode node2;
  private Side side;
  private float fraction;
  private int offset;
  private boolean handle;
}
