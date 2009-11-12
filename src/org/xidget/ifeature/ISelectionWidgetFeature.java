/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ISelectionWidgetFeature.java
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

import java.util.List;
import org.xmodel.IModelObject;

/**
 * An interface for getting and setting the selection of a widget.
 */
public interface ISelectionWidgetFeature
{
  /**
   * Insert the selected element at the specified index in the selection.
   * @param index The index.
   * @param element The element.
   */
  public void insertSelected( int index, IModelObject element);
  
  /**
   * Remove the selected element from the selection.
   * @param index The index of the element to remove.
   * @param element The element to be removed.
   */
  public void removeSelected( int index, IModelObject element);
  
  /**
   * Set the selection corresponding to the specified nodes.
   * @param nodes The selected nodes.
   */
  public void setSelection( List<IModelObject> nodes);

  /**
   * Returns the currently selected nodes.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection();
}
