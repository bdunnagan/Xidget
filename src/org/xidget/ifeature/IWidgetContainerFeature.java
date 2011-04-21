/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IWidgetContainerFeature.java
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

import org.xidget.IXidget;
import org.xidget.layout.Margins;

/**
 * An interface for layout and adding and removing widgets from the container represented by the
 * xidget on which this feature is defined. This interface provides an abstraction for widget
 * containment since widget platforms are usually not smart enough to provide a single means of
 * association. For example in Swing, the JTabbedPane uses a different method to define the tabs of
 * the pane.
 */
public interface IWidgetContainerFeature
{
  /**
   * Add the widget of the specified child to the container.
   * @param child The child.
   */
  public void addWidget( IXidget child);

  /**
   * Add the widget of the specified child to the container at the specified index.
   * @param index The index of insertion.
   * @param child The child.
   */
  public void addWidget( int index, IXidget child);
  
  /**
   * Remove the widget of the specified child to the container.
   * @param child The child.
   */
  public void removeWidget( IXidget child);
  
  /**
   * Tell the container to layout its children again. The container will automatically layout
   * its children when it is first made visible.  This method only needs to be called if the
   * layout changes after the container is visible.
   */
  public void relayout();
  
  /**
   * Set the inside margins of this container.
   * @param margins The margins.
   */
  public void setInsideMargins( Margins margins);
  
  /**
   * Returns the inside margins of the container.
   * @return Returns the inside margins of the container.
   */
  public Margins getInsideMargins();
  
  /**
   * Set the spacing between children within this container.
   * @param spacing The spacing.
   */
  public void setSpacing( int spacing);
  
  /**
   * Returns the spacing between children of the container.
   * @return Returns the spacing between children of the container.
   */
  public int getSpacing();
}
