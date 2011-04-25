/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import java.awt.FontMetrics;
import java.util.Collections;
import java.util.List;

/**
 * An algorithm for calculating the size of a widget consisting of a list of 
 * adjacent columns of text (i.e. table or tree).
 */
public final class ColumnSizeCalculator
{
  @SuppressWarnings("unchecked")
  public ColumnSizeCalculator( FontMetrics metrics, int columns)
  {
    this.metrics = metrics;
    
    styles = new ColumnStyle[ columns];
    sorted = new List[ columns];
    widest = new int[ columns];
  }

  /**
   * Specify an absolute (fixed) width for the specified column.
   * @param column The column to be set.
   * @param width The absolute width of the column.
   * @param chars True if width is specified in characters.
   */
  public void setAbsoluteWidth( int column, int width, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.absolute;
    
    style.minimum = width;
    if ( chars)
    {
      int charWidth = metrics.charWidth( 'X');
      style.minimum *= charWidth;
    }
    
    style.maximum = style.minimum;
    
    styles[ column] = style;
  }
  
  /**
   * Specify that the given column size be assigned.
   * @param column The column to be set.
   */
  public void setFreeWidth( int column)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.free;
    styles[ column] = style;
  }
  
  /**
   * Specify a relative width for the specified column.
   * @param column The column to be set.
   * @param mode The column resize mode.
   * @param maximum The maximum column width.
   * @param relative The percentage of the total width (for relative mode only).
   * @param chars True if width is specified in characters.
   */
  public void setRelativeWidth( int column, int maximum, double relative, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.relative;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = metrics.charWidth( 'X');
      style.maximum *= charWidth;
    }
    style.relative = relative;
    styles[ column] = style;
  }
  
  /**
   * Specify automatic width for the specified column.
   * @param column The column.
   * @param minimum The minimum column width.
   * @param maximum The maximum column width.
   * @param chars True if width is specified in characters.
   */
  public void setAutoWidth( int column, int minimum, int maximum, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.auto;
    style.minimum = minimum;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = metrics.charWidth( 'X');
      style.minimum *= charWidth;
      style.maximum *= charWidth;
    }
    styles[ column] = style;
  }
  
  /**
   * Set the total width.
   * @param width The width.
   */
  public void setWidth( int width)
  {
    this.width = width;
  }
  
  /**
   * Notify the algorithm that the specified text has been added.
   * @param row The row where the text was added.
   * @param column The column where the text was added.
   * @param text The text that was added.
   */
  public void setColumnText( int row, int column, String text)
  {
    if ( row > rows.size()) insertRow( row);
    
    int width = getWidth( text);
    Integer[] widths = rows.get( row);
    widths[ column] = width;
    
    int index = Collections.binarySearch( sorted[ column], width);
    sorted[ column].add( index, row);
  }
  
  /**
   * Insert a row.
   * @param row The row.
   */
  public void insertRow( int row)
  {
    for( int i=rows.size(); i<=row; i++)
    {
      Integer[] widths = new Integer[ styles.length];
      rows.add( row, widths);
    }
  }
  
  /**
   * Notify the algorithm that a row was removed.
   * @param row The row.
   */
  public void removeRow( int row)
  {
    Integer[] widths = rows.get( row);
    for( int i=0; i<styles.length; i++)
    {
      if ( widths[ i] == widest[ i])
      {
        int shorter = sorted[ i].get( row);
        widest[ i] = shorter;
        sorted[ i].remove( row);
      }
    }
    rows.remove( row);
  }

  /**
   * Returns the width of the specified text.
   * @param text The text.
   * @return Returns the width of the specified text.
   */
  protected int getWidth( String text)
  {
    return metrics.stringWidth( text);
  }
  
  /**
   * Compute the width of each column.
   * @param width Returns the computed width of each column.
   * @param padding The amount of additional space added to each computed width.
   * @return Returns the width of each column.
   */
  public void compute( int[] widths, int padding)
  {
    double total = width;
    int columns = styles.length;
    
    // subtract absolute and auto widths from total width
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.absolute)
      {
        widths[ i] = styles[ i].minimum;
        total -= widths[ i] + padding;
        columns--;
      }
      else if ( styles[ i].mode == Mode.auto)
      {
        widths[ i] = widest[ i];
        if ( styles[ i].minimum >= 0 && widths[ i] < styles[ i].minimum) 
        {
          widths[ i] = styles[ i].minimum;
        }
        else if ( styles[ i].maximum >= 0 && widths[ i] > styles[ i].maximum) 
        {
          widths[ i] = styles[ i].maximum;
        }
        total -= widths[ i] + padding;
        columns--;
      }
    }
    
    // subtract relative widths in order from total width
    double basis = total;
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.relative)
      {
        double width = basis * styles[ i].relative;
        widths[ i] = (int)Math.round( width);
        if ( styles[ i].maximum >= 0 && widths[ i] > styles[ i].maximum) 
        {
          widths[ i] = styles[ i].maximum;
        }
        total -= width + padding;
        columns--;
      }
    }
      
    // distribute remaining space
    int width = (int)Math.floor( total / columns);
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.free)
      {
        widths[ i] = width;
      }
    }
  }
  
  private enum Mode { absolute, relative, free, auto};
  
  private final static class ColumnStyle
  {
    public Mode mode;
    public int minimum;
    public int maximum;
    public double relative;
  }
  
  private FontMetrics metrics;
  private ColumnStyle[] styles;
  private List<Integer[]> rows;
  private List<Integer>[] sorted;
  private int[] widest;
  private int width;
}
