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
   * Returns the node of the specified type for this xidget as follows:
   * @param type The type of node.
   * @return Returns null or the widget compute node of the specified type.
   */
  public IComputeNode getComputeNode( Type type);
}
