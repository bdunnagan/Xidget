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
 * An interface for interpreting and handling key bindings. This interface is defined in terms
 * of a xidget-specific key sequence string and an IXAction to be executed when the key sequence
 * is pressed. The key sequence string consists of one or more modifiers separated by a "+" sign
 * followed by one or more primary keys separated by commas. The modifiers are separated by a 
 * comma from the primary keys. For example, the key sequence string, "shift+control,a,b", contains
 * two modifiers and two primaries.
 * <p>
 * This interface defines the key strings for all non-printable characters. These strings should 
 * be referenced by the platform implementation of xidgets to provide a xidget-specific, cross-
 * platform mechanism for defining key bindings.
 */
public interface IKeyFeature
{
  public enum Key
  {
    comma,
    
    f1,
    f2,
    f3,
    f4,
    f5,
    f6,
    f7,
    f8,
    f9,
    f10,
    f11,
    f12,
    f13,
    f14,
    f15,
    f16,
    f17,
    f18,
    f19,
    f20,
    f21,
    f22,
    f23,
    f24,
    
    alt,
    altgraph,
    control,
    meta,
    shift,
    
    tab,
    escape,
    backspace,
    enter,
    
    capslock,
    numlock,
    scrolllock,
    
    home,
    end,
    insert,
    pageup,
    pagedown,
    
    cut,
    paste,
    copy,
    
    multiply,
    subtract,
    
    accept,
    again,
    allcandidates,
    alphanumeric,
    begin,
    cancel,
    clear,
    contextmenu,
    convert,
        
    eurosign,
    find,
    fullwidth,
    halfwidth,
    help,
        
    keypadup,
    keypadleft,
    keypadright,
    keypaddown,
    
    up,
    left,
    right,
    down,
    
    numpad0,
    numpad1,
    numpad2,
    numpad3,
    numpad4,
    numpad5,
    numpad6,
    numpad7,
    numpad8,
    numpad9,
    
    pause,
    printscreen,
    separator,
    space,
    stop,
    undo,
    windows
  }
  
  /**
   * Add the specified key-binding.
   * @param keys The keys sequence to be bound.
   * @param override True if normal processing of key should be skipped.
   * @param script The script to be executed.
   */
  public void bind( String keys, boolean override, IXAction script);
  
  /**
   * Remove the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void unbind( String keys, IXAction script);
}
