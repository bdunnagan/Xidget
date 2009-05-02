/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.tree;

import java.util.List;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for operating on the widget of a tree xidget. This interface assumes
 * that the widget supports multi-column trees, also known as table-trees.  Note that
 * the interface specifies a locus with a single instance of Row.  This is because
 * the Row data-structure holds a reference to its parent and thus the complete path
 * of the node can be determined.
 */
public interface ITreeWidgetFeature
{
  /**
   * Insert rows into the table.
   * @param parent The parent row (contains the parent context).
   * @param rowIndex The index of the first row.
   * @param rows The rows to be inserted.
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows);
  
  /**
   * Remove rows from the table.
   * @param parent The parent row (contains the parent context).
   * @param rowIndex The index of the first row.
   * @param rows The rows that were removed.
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows);
  
  /**
   * Returns the children of the specified row.
   * @param parent The row.
   * @return Returns the children of the specified row.
   */
  public List<Row> getChildren( Row parent);
  
  /**
   * Returns the row with the specified context instance.
   * @param context The context instance.
   * @return Returns the row with the specified context instance.
   */
  public Row findRow( StatefulContext context);
  
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
