/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * A proportional node that can be grabbed by the mouse and responds to the x-coordinate.
 */
public class YGrabNode extends ComputeNode
{
  public YGrabNode( IComputeNode node, float percent, int offset)
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
   * @see org.xidget.layout.ComputeNode#mouseGrab(int, int)
   */
  @Override
  public Grab mouseGrab( int x, int y)
  {
    int dy = y - value;
    if ( dy > -tolerance && dy < tolerance) return Grab.y;
    return Grab.none;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#move(float, float)
   */
  @Override
  public void move( float px, float py)
  {
    percent = py;
    value = (int)Math.round( value * percent) + offset;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "ygrab( ");
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

  private final static int tolerance = 3;
  
  private float percent;
  private int offset;
  private int value;
}