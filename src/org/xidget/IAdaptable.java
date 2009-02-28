/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

/**
 * An interface for classes which support the adapter semantic.
 */
public interface IAdaptable
{
  /**
   * Returns null or an instance of the specified adapter.
   * @param clss The interface class.
   * @return Returns null or an instance of the specified adapter.
   */
  public Object getAdapter( Class<? extends Object> clss);  
}
