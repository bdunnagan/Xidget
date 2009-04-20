/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Column;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for binding the column set of a table.
 */
public interface IColumnSetFeature
{
  /**
   * Insert the specified row into the table.
   * @param row The row.
   * @param context The row context.
   * @return Returns the new row.
   */
  public void bind( Row row, StatefulContext context);
  
  /**
   * Remove the specified row from the table.
   * @param row The row.
   */
  public void unbind( Row row);
  
  /**
   * Returns the columns of the table.
   * @return Returns the columns of the table.
   */
  public List<Column> getColumns();
}
