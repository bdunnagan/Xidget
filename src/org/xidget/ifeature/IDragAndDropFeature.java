/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IDragAndDropFeature.java
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
 * An interface for xidgets that support drag-and-drop.
 */
public interface IDragAndDropFeature
{
  /**
   * Returns true if the xidget supports drag operations.
   * @return Returns true if the xidget supports drag operations.
   */
  public boolean isDragEnabled();
  
  /**
   * Returns true if the xidget supports drop operations.
   * @return Returns true if the xidget supports drop operations.
   */
  public boolean isDropEnabled();
  
  /**
   * Returns true if the specified node can be dragged.
   * @param context The context.
   * @return Returns true if the specified node can be dragged.
   */
  public boolean canDrag( StatefulContext context);
  
  /**
   * Returns true if the specified node can be dropped on the associated xidget.
   * @param context The context.
   * @return Returns true if the specified node can be dropped on the associated xidget.
   */
  public boolean canDrop( StatefulContext context);
  
  /**
   * Drag the specified nodes from this xidget.
   * @param context The context.
   */
  public void drag( StatefulContext context);
  
  /**
   * Drop the specified nodes on this xidget.
   * @param context The context.
   */
  public void drop( StatefulContext context);
}
