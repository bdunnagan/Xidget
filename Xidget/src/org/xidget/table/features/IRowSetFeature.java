/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.List;
import org.xidget.IXidget;
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
   * Add a column xidget.
   * @param xidget The xidget.
   */
  public void addColumn( IXidget xidget);
  
  /**
   * Set the new row-set of the table.
   * @param context The parent context.
   * @param rows The row objects.
   */
  public void setRows( StatefulContext context, List<IModelObject> rows);
  
  /**
   * Returns the index of the current row being bound.
   * @return Returns the index of the current row being bound.
   */
  public int getCurrentRow();
}
