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
   * Set the variable that stores the selection.
   * @param context The parent context.
   * @param variable The variable name.
   */
  public void setVariable( StatefulContext context, String variable);
  
  /**
   * Insert the selected object into the selection model.
   * @param context The parent context.
   * @param index The index of the insertion.
   * @param object The object.
   */
  public void insertSelected( StatefulContext context, int index, Object object);
  
  /**
   * Remove the selected object from the selection model.
   * @param context The parent context.
   * @param index The index of the removals.
   * @param object The object.
   */
  public void removeSelected( StatefulContext context, int index, Object object);
  
  /**
   * Set the selection corresponding to the specified nodes.
   * @param context The parent context.
   * @param nodes The selected nodes.
   */
  public void setSelection( StatefulContext context, List<? extends Object> nodes);
  
  /**
   * Returns the currently selected nodes.
   * @param context The parent context.
   * @return Returns the currently selected nodes.
   */
  public List<? extends Object> getSelection( StatefulContext context);
  
  /**
   * Returns an identify for the object that may be different from the object. The selection model defines
   * an identity based on the type of object reference requested. This is only applicable for IModelObject
   * references stored under a selection parent.
   * @param object The object.
   * @return Returns a unique identity for the object.
   */
  public Object getIdentity( Object object);
}
