/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.tree;

import org.xidget.table.Row;

/**
 * An interface for expanding and collapsing the data-model of a tree xidget.
 * This feature is used by the widget implementation to notify the data-model
 * that a tree node has been expanded or collapsed.  Since the data-model may
 * contain external references, these references should not be examined until
 * the tree explicitly requests it.
 */
public interface ITreeExpandFeature
{
  /**
   * Expand the children of the specified row.
   * @param row The row to be expanded.
   */
  public void expand( Row row);
  
  /**
   * Collapse the specified row.
   * @param row The row to be collapsed.
   */
  public void collapse( Row row);
}
