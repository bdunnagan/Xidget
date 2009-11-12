/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IPlugin.java
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
package org.xidget;

/**
 * An interface for classes that extend the capabilities of the an IToolkit. The IToolkit provides
 * the implementation of the base xidget set for a specific widget platform (e.g. Swing).  A plugin
 * adds one or more new xidgets and optionally provides one or more widget platform implementations.
 */
public interface IPlugin
{
  /**
   * Configure the plugin. This method should customize the TagProcessor of the toolkit to
   * recognize the xidgets and/or tags that are part of the service the plugin provides.
   * @param toolkit The toolkit.
   */
  public void configure( IToolkit toolkit);
}
