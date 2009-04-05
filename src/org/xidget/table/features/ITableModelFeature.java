/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.List;
import org.xmodel.IModelObject;

/**
 * An interface for the data-model of a table xidget.
 */
public interface ITableModelFeature
{
  /**
   * Set the row objects of the table.
   * @param rows The complete set of row objects.
   */
  public void setRows( List<IModelObject> rows);

  /**
   * Returns the rows of the table.
   * @return Returns the rows of the table.
   */
  public List<IModelObject> getRows();
  
  /**
   * Set the source node for the specified cell.
   * @param row The row index of the cell.
   * @param column The column index of the cell.
   * @param channel The text channel.
   * @param node The source node.
   */
  public void setSource( int row, int column, String channel, IModelObject node);
  
  /**
   * Returns null or the node addressed by the specified row and column indices. Only
   * columns whose source expression returns a node-set will return a non-null value.
   * @param row The row index.
   * @param column The column index.
   * @param channel The text channel.
   * @return Returns null or the node addressed by the specified row and column indices.
   */
  public IModelObject getSource( int row, int column, String channel);
  
  /**
   * Sets the text in the specified node.
   * @param row The row index of the node.
   * @param column The column index of the node.
   * @param channel The text channel.
   * @param text The new text.
   */
  public void setText( int row, int column, String channel, String text);
}
