/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
