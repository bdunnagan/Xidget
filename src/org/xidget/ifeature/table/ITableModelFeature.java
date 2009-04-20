/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import org.xidget.table.Row;
import org.xmodel.IModelObject;

/**
 * An interface for the data-model of a table xidget.
 */
public interface ITableModelFeature
{
  /**
   * Returns null or the node addressed by the specified row and column. Only columns 
   * whose source expression returns a node-set will return a non-null value.
   * @param row The object representing the row.
   * @param column The column index.
   * @return Returns null or the node addressed by the specified row and column indices.
   */
  public IModelObject getSource( Row row, int column);
  
  /**
   * Sets the text in the specified node.
   * @param row The object representing the row.
   * @param column The column index of the node.
   * @param text The new text.
   */
  public void setText( Row row, int column, String text);
}
