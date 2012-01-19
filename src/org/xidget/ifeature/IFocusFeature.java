/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xidget.IXidget;

/**
 * An interface for getting, setting and tracking focus changes.  
 * This feature should only be implemented by IToolkit.
 */
public interface IFocusFeature
{
  /**
   * Set the focus to the specified xidget.
   * @param xidget The xidget.
   */
  public void setFocus( IXidget xidget);
  
  /**
   * @return Returns the xidget that currently has focus.
   */
  public IXidget getFocus();
  
  /**
   * Add a listener on focus change events.
   * @param listener The listener.
   */
  public void addFocusListener( IFocusListener listener);
  
  /**
   * Remove a listener on focus change events. 
   * @param listener The listener.
   */
  public void removeFocusListener( IFocusListener listener);
  
  /**
   * An interface for receiving notification of the currently focused xidget.
   */
  public static interface IFocusListener
  {
    /**
     * Called when the xidget with focus changes.
     * @param newFocus The xidget that gained focus (may be null).
     * @param oldFocus The xidget that lost focus (may be null).
     */
    public void notifyFocus( IXidget newFocus, IXidget oldFocus);
  }
}
