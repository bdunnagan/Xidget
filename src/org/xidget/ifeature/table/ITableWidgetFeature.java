/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import org.xidget.table.Row;

/**
 * An interface for operating on the widget of a table xidget.
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
   * @param rowIndex The index of the first row to be inserted.
   * @param count The number of rows to insert.
   */
  public void insertRows( int rowIndex, int count);
  
  /**
   * Remove rows from the table.
   * @param rowIndex The index of the first row to be removed.
   * @param count The number of rows to remove.
   */
  public void removeRows( int rowIndex, int count);
  
  /**
   * Set the title of the specified column.
   * @param columnIndex The column index.
   * @param title The title.
   */
  public void setTitle( int columnIndex, String title);
  
  /**
   * Set the text of the specified cell.
   * @param row The object representing the row.
   * @param columnIndex The column index of the cell.
   * @param text The text.
   */
  public void setText( Row row, int columnIndex, String text);
    
  /**
   * Set the icon of the specified cell.
   * @param row The object representing the row.
   * @param columnIndex The column index of the cell.
   * @param icon The platform icon object.
   */
  public void setIcon( Row row, int columnIndex, Object icon);
}
