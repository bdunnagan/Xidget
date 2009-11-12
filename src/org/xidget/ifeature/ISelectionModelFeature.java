/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ISelectionModelFeature.java
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
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for managing the selection model.
 */
public interface ISelectionModelFeature
{
  /**
   * Configure this feature from the specified element.
   * @param element The element.
   */
  public void configure( IModelObject element);
  
  /**
   * Set the parent of the selection.
   * @param context The parent context.
   * @param element The selection parent element.
   */
  public void setParent( StatefulContext context, IModelObject element);
  
  /**
   * Insert the selected element into the selection model.
   * @param context The parent context.
   * @param index The index of the insertion.
   * @param element The element.
   */
  public void insertSelected( StatefulContext context, int index, IModelObject element);
  
  /**
   * Remove the selected element from the selection model.
   * @param context The parent context.
   * @param index The index of the removals.
   * @param element The element.
   */
  public void removeSelected( StatefulContext context, int index, IModelObject element);
  
  /**
   * Set the selection corresponding to the specified nodes.
   * @param context The parent context.
   * @param nodes The selected nodes.
   */
  public void setSelection( StatefulContext context, List<IModelObject> nodes);
  
  /**
   * Returns the currently selected nodes.
   * @param context The parent context.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection( StatefulContext context);
  
  /**
   * Returns a unique identity for the node. 
   * @param node The node.
   * @return Returns a unique identity for the node.
   */
  public Object getIdentity( IModelObject node);
}
