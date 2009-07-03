/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xidget.layout.IComputeNode;

/**
 * An interface for accessing IComputeNode instances from a xidget.
 */
public interface IComputeNodeFeature
{
  public enum Type { top, left, right, bottom, width, height, none};
  
  /**
   * Returns the node of the specified type for the associated xidget. The actual type of node
   * returned may be different if the xidget is a container. Containers have inside and outside
   * nodes. A request for the TOP of the inside of a container will return a ConstantNode with
   * zero offset, for example.
   * @param type The type of node.
   * @param container True if a node for the inside of a container should be returned.
   * @return Returns null or the widget compute node of the specified type.
   */
  public IComputeNode getComputeNode( Type type, boolean container);
}
