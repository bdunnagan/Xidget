/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for performing targeted updates to the rows of a table given 
 * a complete row-set. The implementation will typically perform a shallow
 * diff of the new row-set with the old row-set.
 */
public interface IRowSetFeature
{
  /**
   * Set the rows of the table.
   * @param context The parent context.
   * @param rows The row objects.
   */
  public void setRows( StatefulContext context, List<IModelObject> rows);
  
  /**
   * Returns the number of rows in this row-set.
   * @return Returns the number of rows in this row-set.
   */
  public int getRowCount();
  
  /**
   * Returns the row at the specified index.
   * @param index The index.
   * @return Returns the row at the specified index.
   */
  public Row getRow( int index);
  
  /**
   * Returns the index of the specified row.
   * @param row The row.
   * @return Returns -1 or the index of the specified row.
   */
  public int getRowIndex( Row row);
}
