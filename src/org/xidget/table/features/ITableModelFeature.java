/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import org.xmodel.IModelObject;

/**
 * An interface for the data-model of a table xidget. Since a table xidget provides
 * a tabular transform of its source data, some of that data will be cached. This
 * interface provides access to this cached data. This interface also provides 
 * abstraction from the table xidget schema for the tabular data.  It does not
 * provide information about the column header title and image, however.
 * <p>
 * The <code>setText</code> method should use the ITextModelFeature associated
 * with the specified column to process the raw widget-supplied text before it is
 * set in the data-model. The ITextModelFeature is addressed by getting the ColumnXidget 
 * child corresponding to the column index.  
 */
public interface ITableModelFeature
{
  /**
   * Returns the row object for the specified row.
   * @param row The row index.
   * @return Returns the row object for the specified row.
   */
  public IModelObject getRow( int row);
  
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
