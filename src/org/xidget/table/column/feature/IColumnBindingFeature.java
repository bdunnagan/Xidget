/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column.feature;

import org.xmodel.IModelObject;

/**
 * An interface for binding the columns of a table to a row object. The implementation
 * of this interface is responsible for updating the table widget and keeping the rows
 * updated.
 */
public interface IColumnBindingFeature
{
  /**
   * Bind the specified row object with the specified row index.
   * @param row The row index.
   * @param object The row object.
   */
  public void bind( int row, IModelObject object);
  
  /**
   * Unbind the specified row object.
   * @param row The row index.
   * @param object The row object.
   */
  public void unbind( int row, IModelObject object);
}
