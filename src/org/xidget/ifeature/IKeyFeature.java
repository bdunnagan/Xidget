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
 * An interface for interpreting and handling key bindings. The key sequence array consists
 * of one or more keys including any modifiers. The key binding engine is a platform independent
 * engine that will be documented elsewhere. As an example, however, the following key sequence
 * will be valid: { "control", "f1"}.
 * The script string identifies the script to the IScriptFeature.
 */
public interface IKeyFeature
{
  /**
   * Add the specified key-binding.
   * @param keys The keys sequence to be bound.
   * @param script The script to be executed.
   */
  public void bind( String keys, IXAction script);
  
  /**
   * Remove the specified key-binding.
   * @param keys The key configuration string.
   * @param script The script to be executed.
   */
  public void unbind( String keys, IXAction script);
}
