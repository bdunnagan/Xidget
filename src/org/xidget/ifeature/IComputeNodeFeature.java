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
  /**
   * Returns the anchor of the specified type for this xidget as follows:
   * <ul>
   * <li><i>x0</i> - WidgetLeftNode 
   * <li><i>y0</i> - WidgetTopNode
   * <li><i>x1</i> - WidgetRightNode 
   * <li><i>y1</i> - WidgetBottomNode
   * <li><i>w</i> - WidgetWidthNode
   * <li><i>h</i> - WidgetHeightNode
   * </ul>
   * @param type The type of anchor.
   * @return Returns null or the widget anchor of the specified type.
   */
  public IComputeNode getAnchor( String type);
  
  /**
   * Returns the anchor of a parent container for use by its children.
   * @param type The type of anchor.
   * @return Returns null or the anchor of a parent container for use by its children.
   */
  public IComputeNode getParentAnchor( String type);
}
