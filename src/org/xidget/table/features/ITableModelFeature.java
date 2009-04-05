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
   * Returns null or the node addressed by the specified row and column indices. Only
   * columns whose source expression returns a node-set will return a non-null value.
   * @param row The row index.
   * @param column The column index.
   * @return Returns null or the node addressed by the specified row and column indices.
   */
  public IModelObject getNode( int row, int column);
  
  /**
   * Returns the text in the specified node.
   * @param row The row index of the node.
   * @param column The column index of the node.
   * @return Returns the text in the specified node.
   */
  public String getText( int row, int column);
  
  /**
   * Sets the text in the specified node.
   * @param row The row index of the node.
   * @param column The column index of the node.
   * @param text The new text.
   */
  public void setText( int row, int column, String text);

  /**
   * Returns the icon for the specified node.
   * @param row The row index of the node.
   * @param column The column index of the node.
   * @return Returns the icon for the specified node.
   */
  public Object getIcon( int row, int column);
}
