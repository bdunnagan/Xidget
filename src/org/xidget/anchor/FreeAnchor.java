/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.anchor;

import org.xidget.layout.V2D;

/**
 * An implementation of IAnchor for an anchor that can be moved during layout.
 */
public class FreeAnchor extends AbstractAnchor
{
  /**
   * Set the location of the anchor.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */
  public void setPoint( int x, int y)
  {
    point.x = x;
    point.y = y;
    notifyMove();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IAnchor#getPoint()
   */
  public V2D getPoint()
  {
    return point;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IAnchor#moveTo(int, int)
   */
  public boolean moveTo( int x, int y)
  {
    point.x = x;
    point.y = y;
    return true;
  }
  
  private V2D point;
}
