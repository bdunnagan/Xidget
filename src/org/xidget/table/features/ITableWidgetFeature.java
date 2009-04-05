/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import org.xidget.IXidget;

/**
 * An interface for operating on the widget of a table xidget.
 */
public interface ITableWidgetFeature
{
  /**
   * Set the xidget used for editing cells in the specified column.
   * @param column The column.
   * @param xidget The xidget.
   */
  public void setEditor( int column, IXidget xidget);
  
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
