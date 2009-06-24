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
  public enum Type { top, left, right, bottom, width, height, horizontal_center, vertical_center};
  
  /**
   * Returns the anchor of the specified type for this xidget as follows:
   * @param type The type of anchor.
   * @return Returns null or the widget anchor of the specified type.
   */
  public IComputeNode getAnchor( Type type);
  
  /**
   * Returns the anchor of a parent container for use by its children.
   * @param type The type of anchor.
   * @return Returns null or the anchor of a parent container for use by its children.
   */
  public IComputeNode getParentAnchor( Type type);
  
  /**
   * Resets all of the parent anchors. This method should be called when the xidget hierarchy changes.
   */
  public void clearParentAnchors();
}
