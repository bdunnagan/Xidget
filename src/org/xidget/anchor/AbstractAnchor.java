/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.anchor;

import java.util.ArrayList;
import java.util.List;
import org.xidget.layout.IAnchor;

/**
 * Convenient base class for anchor implementations.
 */
public abstract class AbstractAnchor implements IAnchor
{
  protected AbstractAnchor()
  {
    listeners = new ArrayList<IListener>( 2);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IAnchor#addListener(org.xidget.layout.IAnchor.IListener)
   */
  public void addListener( IListener listener)
  {
    if ( !listeners.contains( listener))
      listeners.add( listener);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IAnchor#removeListener(org.xidget.layout.IAnchor.IListener)
   */
  public void removeListener( IListener listener)
  {
    listeners.remove( listener);
  }

  /**
   * Notify listeners that the anchor has moved.
   */
  protected void notifyMove()
  {
    for( IListener listener: listeners)
      listener.onMove( this);
  }

  private List<IListener> listeners;
}
