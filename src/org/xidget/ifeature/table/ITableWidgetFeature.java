/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Row;

/**
 * An interface for operating on the widget of a table xidget. This interface requires
 * that the widget table implementation keep track of data-structures created by the
 * xidget framework.
 */
public interface ITableWidgetFeature
{
  /**
   * Set whether the specified cell is editable.
   * @param rowIndex The row index of the cell.
   * @param columnIndex The column index of the cell.
   * @param editable True if the cell is editable.
   */
  public void setEditable( int rowIndex, int columnIndex, boolean editable);
  
  /**
   * Insert rows into the table.
   * @param rowIndex The index of the first row.
   * @param rows The rows to be inserted.
   */
  public void insertRows( int rowIndex, Row[] rows);
  
  /**
   * Remove rows from the table.
   * @param rowIndex The index of the first row.
   * @param count The number of rows to remove.
   */
  public void removeRows( int rowIndex, int count);
  
  /**
   * Returns the rows of the table.
   * @return Returns the rows of the table.
   */
  public List<Row> getRows();
  
  /**
   * Set the title of the specified column.
   * @param columnIndex The column index.
   * @param title The title.
   */
  public void setTitle( int columnIndex, String title);

  /**
   * Update the specified cell.
   * @param row The row.
   * @param columnIndex The column index.
   */
  public void updateCell( Row row, int columnIndex);
}
