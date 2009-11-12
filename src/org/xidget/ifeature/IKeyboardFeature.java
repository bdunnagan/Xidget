/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IKeyboardFeature.java
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

import org.xmodel.xaction.IXAction;

/**
 * An interface for interpreting and handling key bindings.
 */
public interface IKeyboardFeature
{
  /**
   * Add the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void addKeyPressedBinding( String keys, IXAction script);
  
  /**
   * Remove the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void removeKeyPressedBinding( String keys, IXAction script);
  
  /**
   * Add the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void addKeyReleasedBinding( String keys, IXAction script);
  
  /**
   * Remove the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void removeKeyReleasedBinding( String keys, IXAction script);
  
  /**
   * Called by the platform when a key is pressed.
   * @param code The key code.
   */
  public void keyPressed( int code);
  
  /**
   * Called by the platform when a key is released.
   * @param code The key code.
   */
  public void keyReleased( int code);
  
  /**
   * Translate the specified key configuration string into an array of key codes.
   * @param keys The key configuration string.
   * @return Returns an array of key codes.
   */
  public int[] translate( String keys);
}
