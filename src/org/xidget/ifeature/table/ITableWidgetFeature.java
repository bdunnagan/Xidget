/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

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
   * @param parent The parent context.
   * @param rowIndex The index of the first row.
   * @param rows The rows to be inserted.
   */
  public void insertRows( StatefulContext parent, int rowIndex, Row[] rows);
  
  /**
   * Remove rows from the table.
   * @param parent The parent context.
   * @param rowIndex The index of the first row.
   * @param rows The rows that were removed.
   */
  public void removeRows( StatefulContext parent, int rowIndex, Row[] rows);
  
  /**
   * Returns the rows of the table. The parent context is only needed for tree
   * implementations since this feature is bound to a different context for
   * each parent row whose children it supplies.
   * @param parent The parent context.
   * @return Returns the rows of the table.
   */
  public List<Row> getRows( StatefulContext parent);
  
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
