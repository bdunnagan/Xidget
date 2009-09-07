/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
