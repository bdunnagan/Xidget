/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ISourceFeature.java
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

import org.xmodel.IModelObject;

/**
 * An interface for accessing the source node for a xidget. The source node is a single
 * node that the xidget uses to get and store the value to be displayed.
 */
public interface ISourceFeature
{
  /**
   * Set the source node.
   * @param node The source node.
   */
  public void setSource( IModelObject node);
  
  /**
   * Get the source node.
   * @return Returns null or the source node.
   */
  public IModelObject getSource();
}
