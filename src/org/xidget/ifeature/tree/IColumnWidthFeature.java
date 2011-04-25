/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.tree;

/**
 * An interface for a feature that computes the widths of a table or tree.
 */
public interface IColumnWidthFeature
{
  /**
   * Insert a new column.
   * @param column The column.
   */
  public void insertColumn( int column);
  
  /**
   * Remove a column.
   * @param column The column.
   */
  public void removeColumn( int column);
  
  /**
   * Specify an absolute (fixed) width for the specified column.
   * @param column The column to be set.
   * @param width The absolute width of the column.
   * @param chars True if width is specified in characters.
   */
  public void setAbsoluteWidth( int column, int width, boolean chars);
  
  /**
   * Specify that the given column size be assigned.
   * @param column The column to be set.
   */
  public void setFreeWidth( int column);
  
  /**
   * Specify a relative width for the specified column.
   * @param column The column to be set.
   * @param mode The column resize mode.
   * @param maximum The maximum column width.
   * @param relative The percentage of the total width (for relative mode only).
   * @param chars True if width is specified in characters.
   */
  public void setRelativeWidth( int column, int maximum, double relative, boolean chars);
  
  /**
   * Specify automatic width for the specified column.
   * @param column The column.
   * @param minimum The minimum column width.
   * @param maximum The maximum column width.
   * @param chars True if width is specified in characters.
   */
  public void setAutoWidth( int column, int minimum, int maximum, boolean chars);
  
  /**
   * Set the total width.
   * @param width The width.
   */
  public void setWidth( int width);
  
  /**
   * Notify the algorithm that the specified text has been added.
   * @param row The row where the text was added.
   * @param column The column where the text was added.
   * @param text The text that was added.
   */
  public void setColumnText( int row, int column, String text);
  
  /**
   * Insert a row.
   * @param row The row.
   */
  public void insertRow( int row);
  
  /**
   * Notify the algorithm that a row was removed.
   * @param row The row.
   */
  public void removeRow( int row);
}
