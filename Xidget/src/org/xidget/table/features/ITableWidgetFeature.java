/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

/**
 * An interface for operating on the widget of a table xidget.
 */
public interface ITableWidgetFeature
{
  /**
   * Set whether the specified cell is editable.
   * @param row The row index of the cell.
   * @param column The column index of the cell.
   * @param editable True if the cell is editable.
   */
  public void setEditable( int row, int column, boolean editable);
  
  /**
   * Insert a row into the table.
   * @param row The index at which the row will be inserted.
   */
  public void insertRow( int row);

  /**
   * Insert rows into the table.
   * @param row The index of the first row to be inserted.
   * @param count The number of rows to insert.
   */
  public void insertRows( int row, int count);
  
  /**
   * Remove a row from the table.
   * @param row The row to be removed.
   */
  public void removeRow( int row);
  
  /**
   * Remove rows from the table.
   * @param row The index of the first row to be removed.
   * @param count The number of rows to remove.
   */
  public void removeRows( int row, int count);
  
  /**
   * Set the text of the specified cell.
   * @param row The row index of the cell.
   * @param column The column index of the cell.
   * @param text The text.
   */
  public void setText( int row, int column, String text);
    
  /**
   * Set the icon of the specified cell.
   * @param row The row index of the cell.
   * @param column The column index of the cell.
   * @param icon The platform icon object.
   */
  public void setIcon( int row, int column, Object icon);
}
