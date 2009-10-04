/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xidget.layout.IComputeNode;

/**
 * An interface for accessing IComputeNode instances from a xidget.
 */
public interface IComputeNodeFeature
{
  public enum Type { top, left, right, bottom, nearest, none};
  
  /**
   * Returns the node of the specified type for the associated xidget. The actual type of node
   * returned may be different if the xidget is a container. Containers have inside and outside
   * nodes. A request for the TOP of the inside of a container will return a ConstantNode with
   * zero offset, for example.
   * @param type The type of node.
   * @param container True if a node for the inside of a container should be returned.
   * @param create True if the node should be created if it does not exist.
   * @return Returns null or the widget compute node of the specified type.
   */
  public IComputeNode getComputeNode( Type type, boolean container, boolean create);
  
  /**
   * Returns the list of nodes that have been requested. It is assumed that if a node is requested
   * then the node should be included in the layout. This is helpful since some nodes may be created
   * in hidden ways.
   * @return Returns the list of nodes that have been requested.
   */
  public List<IComputeNode> getAccessedList();
}
