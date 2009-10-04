/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;

/**
 * An implementation of IComputeNode that functions as an anchor within a container.
 */
public class AnchorNode extends ComputeNode
{
  /**
   * Create an AnchorNode that depends on the specified opposite sides of the inside 
   * of a container.
   * @param container The container.
   * @param type The type of anchor.
   */
  public AnchorNode( IXidget container, Type type)
  {
    this( container, type, 0, 0);
  }
  
  /**
   * Create an AnchorNode that depends on the specified opposite sides of the inside 
   * of a container.
   * @param container The container.
   * @param type The type of anchor.
   * @param fraction The fraction of the container width or height.
   * @param offset The offset from the fraction.
   */
  public AnchorNode( IXidget container, Type type, float fraction, int offset)
  {
    this.container = container;
    this.type = type;
    this.fraction = fraction;
    this.offset = offset;
    
    IComputeNodeFeature feature = container.getFeature( IComputeNodeFeature.class);
    switch( type)
    {
      case left:
      case right:
        super.addDependency( feature.getComputeNode( Type.right, true, true));
        break;
      
      case top:
      case bottom:
        super.addDependency( feature.getComputeNode( Type.bottom, true, true));
        break;
    }
  }
  
  /**
   * Returns the type of this AnchorNode, either width or height.
   * @return Returns Type.width or Type.height.
   */
  public Type getType()
  {
    return type;
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
    if ( type == Type.left || type == Type.right) return handle;
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#hasYHandle()
   */
  @Override
  public boolean hasYHandle()
  {
    if ( type == Type.top || type == Type.bottom) return handle;
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
   * @see org.xidget.layout.ComputeNode#setValue(float)
   */
  public void setValue( float value)
  {
    super.setValue( Math.round( value * fraction + offset));
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "%d. (%s) %s:%s( %%%2.1f, %+d) <- %s", getID(), printValue(), container, type, fraction, offset, printDependencies());
  }

  private IXidget container;
  private Type type;
  private float fraction;
  private int offset;
  private boolean handle;
}
