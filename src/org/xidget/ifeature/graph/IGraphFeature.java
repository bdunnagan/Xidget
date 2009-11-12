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

import java.util.List;
import org.xidget.IXidget;
import org.xmodel.IModelObject;

/**
 * An interface for xidgets that belong to a undirected graph. The term <i>graph</i> does not imply
 * any visual characteristics for the xidget. This feature only supports the description of how
 * a xidget participates in a graph. The identity of an edge is determined by the <i>id</i> of
 * its source and target xidgets.
 * <p>
 * This feature can support directed graphs if the node-set associated with each edge contains
 * the direction information.
 */
public interface IGraphFeature
{
  /**
   * Create, update or delete a directed edge from the specified source xidget to the specified
   * target xidget. An edge exists iff there is at least one node in the specified node-set. If
   * the edge does not exist and this method is called with a non-empty node-set, then the edge
   * is created. If the edge exists and this method is called with an empty node-set, then the
   * edge is removed. Otherwise, the edge is updated with the new nodes.
   * @param xidget1 A xidget.
   * @param xidget2 A xidget.
   * @param nodes The node-set.
   */
  public void updateEdge( IXidget xidget1, IXidget xidget2, List<IModelObject> nodes);
  
  /**
   * Returns the node-set associated with the specified edge.
   * @param xidget1 A xidget.
   * @param xidget2 A xidget.
   * @return Returns an empty list or the node-set associated with the specified edge.
   */
  public List<IModelObject> getEdge( IXidget xidget1, IXidget xidget2);
  
  /**
   * Returns a list of all the xidgets that share an edge with the specified xidget.
   * @param xidget The xidget.
   * @return Returns a list of all the xidgets that share an edge with the specified xidget.
   */
  public List<IXidget> getConnected( IXidget xidget);
}
