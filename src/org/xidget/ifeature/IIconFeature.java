/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IIconFeature.java
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

/**
 * An interface for setting an icon. This feature is only appropriate 
 * for xidgets which only display a single icon. It is used by the 
 * IconBindingRule.
 */
public interface IIconFeature
{
  /**
   * Set the xidget icon.
   * @param The xidget icon.
   */
  public void setIcon( Object icon);
}
