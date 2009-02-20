/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An anchor point on a xidget to which an attachment can be made.
 */
public interface IAnchor
{
  /**
   * Returns the current location of the anchor.
   * @return Returns the current location of the anchor.
   */
  public V2D getPoint();
  
  /**
   * Move this anchor to the specified point. The implementation may opt not to
   * change one or both of the coordinates. The layout algorithm uses the return
   * value to efficiently ignore unchanged parts of the tree.
   * @param x The new x-coordinate.
   * @param y The new y-coordinate.
   * @return Returns true if the position was changed.
   */
  public boolean moveTo( int x, int y);
  
  /**
   * Add a listener to the anchor.
   * @param listener The listener.
   */
  public void addListener( IListener listener);
  
  /**
   * Remove a listener from the anchor.
   * @param listener The listener.
   */
  public void removeListener( IListener listener);
  
  /**
   * An interface for observing changes to the location of the anchor.
   */
  public interface IListener
  {
    /**
     * Called when the location of the anchor moves.
     * @param anchor The anchor whose location has changed.
     */
    public void onMove( IAnchor anchor);
  }
}
