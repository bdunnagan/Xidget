/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IAsyncFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.ifeature;

import java.lang.reflect.InvocationTargetException;

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
   * Run the specified runnable in the UI thread and block for its execution.
   * @param runnable The runnable.
   */
  public void runWait( Runnable runnable) throws InvocationTargetException, InterruptedException;
  
  /**
   * Schedule the specified runnable to execute in the UI thread after the specified delay.
   * Each time this method is called for a given key, the timer instance delay is reset.
   * @param key The key.
   * @param delay The delay in milliseconds.
   * @param repeat True for periodic execution.
   * @param runnable The runnable.
   */
  public void schedule( Object key, int delay, boolean repeat, Runnable runnable);
  
  /**
   * Cancel the specified timer instance.
   * @param key The key.
   */
  public void cancel( Object key);
}
