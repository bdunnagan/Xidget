/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IWidgetCreationFeature.java
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
 * An interface for creating and deleting the widget (or widgets) associated with a xidget.
 * It is usually convenient to implement this interface with another feature interface that
 * provides access to the widget that was created.
 */
public interface IWidgetCreationFeature
{
  /**
   * Create the widget or widgets for the associated xidget. This method
   * does not create widgets for the children of the xidget.
   */
  public void createWidgets();
  
  /**
   * Destroy the widget or widgets created for this xidget.  When a xidget is destroyed, this
   * method is only called for the root of the hierarchy.
   */
  public void destroyWidgets();
  
  /**
   * Returns the widgets which created by the last call to the <code>createWidgets</code> method.
   * The widgets must be configured into a hierarchy with a single top-level widget.  The top-level
   * widget must be the first widget in the array.
   * @return Returns the widgets which created by the last call to the <code>createWidgets</code> method.
   */
  public Object[] getLastWidgets();
}
