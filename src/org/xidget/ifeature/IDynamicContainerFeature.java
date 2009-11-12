/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IDynamicContainerFeature.java
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
 * An interface for containers that support dynamic addition and removal of children, like a tab form.
 * This feature is not part of the dynamic rebuilding capability of the framework, which is a lower-level
 * capability.  This feature might be implemented using that capability, however.
 */
public interface IDynamicContainerFeature
{
  /**
   * Update the list of children for this container. The implementation is responsible for insuring
   * that there are zero or one child for each node and that children are inserted into the container
   * in the correct order.
   * @param context The parent context. 
   * @param nodes The nodes.
   */
  public void setChildren( StatefulContext context, List<IModelObject> nodes);
  
  /**
   * Returns the list of children for this container.
   * @return Returns the list of children for this container.
   */
  public List<IModelObject> getChildren();
}
