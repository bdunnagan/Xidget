/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IWidgetContextFeature.java
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

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for accessing the context associated with a widget. Widgets are created by the
 * IWidgetCreationFeature.  They are created before the xidget is bound via the IBindFeature.
 * Therefore, there is usually one widget per context.
 */
public interface IWidgetContextFeature
{
  /**
   * Set the context associated with the specified widget. 
   * @param widget The platform-specific widget object.
   * @param context The context.
   */
  public void createAssociation( Object widget, StatefulContext context);
  
  /**
   * Remove the specified widget-context association.
   * @param widget The platform-specific widget object.
   */
  public void removeAssociation( Object widget);
    
  /**
   * Returns the context associated with the specified widget.
   * @param widget The platform-specific widget object.
   * @return Returns the context associated with the specified widget.
   */
  public StatefulContext getContext( Object widget);
}
