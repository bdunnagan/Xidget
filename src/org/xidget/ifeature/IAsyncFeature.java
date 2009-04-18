/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for asynchronous and timed execution on the UI thread.
 */
public interface IAsyncFeature
{
  /**
   * Run the specified runnable in the UI thread.
   * @param runnable The runnable.
   */
  public void run( Runnable runnable);
  
  /**
   * Schedule the specified runnable to execute in the UI thread after the specified delay.
   * Each time this method is called for a given key, the timer instance delay is reset.
   * @param key The key.
   * @param delay The delay in milliseconds.
   * @param runnable The runnable.
   */
  public void schedule( Object key, int delay, Runnable runnable);
  
  /**
   * Cancel the specified timer instance.
   * @param key The key.
   */
  public void cancel( Object key);
}
