/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IGraphFeature.java
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
package org.xidget.ifeature.graph;

import org.xidget.IXidget;
import org.xmodel.IModelObject;

/**
 * An interface for managing edges connected to a xidget that represents a node in a graph.
 */
public interface IEdgeFeature
{
  /**
   * Create an edge to the specified xidget.
   * @param xidget The xidget.
   * @param edge An object representing the edge.
   */
  public void createEdge( IXidget xidget, Object edge);
  
  /**
   * Delete an edge.
   * @param edge The edge.
   */
  public void deleteEdge( Object edge);
}
