/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Column;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the data-model of a table xidget.
 */
public interface ITableModelFeature
{
  /**
   * Insert the specified row into the table.
   * @param index The index of the row.
   * @param context The row context.
   * @return Returns the new row.
   */
  public Row insertRow( int index, StatefulContext context);
  
  /**
   * Remove the specified row from the table.
   * @param index The index of the row.
   */
  public void removeRow( int index);
  
  /**
   * Returns the table rows.
   * @return Returns the table rows.
   */
  public List<Row> getRows();
  
  /**
   * Returns the table columns.
   * @return Returns the table columns.
   */
  public List<Column> getColumns();
  
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
