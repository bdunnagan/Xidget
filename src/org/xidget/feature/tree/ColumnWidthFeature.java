/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.Collections;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.tree.IColumnWidthFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;

/**
 * An implementation of IColumnWidthFeature that provides a very fast and flexible
 * algorithm for computing the width of the columns of a tree, table or other 
 * columnar xidget.
 */
@SuppressWarnings("unchecked")
public abstract class ColumnWidthFeature implements IColumnWidthFeature
{
  protected ColumnWidthFeature( IXidget xidget, int columns)
  {
    this.xidget = xidget;
    styles = new ColumnStyle[ columns];
    sorted = new List[ columns];
    widest = new int[ columns];
    sizes = new int[ columns];
    padding = 3;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#insertColumn(int)
   */
  public final void insertColumn( int column)
  {
    ColumnStyle[] newStyles = new ColumnStyle[ styles.length + 1];
    if ( column > 0) System.arraycopy( styles, 0, newStyles, 0, column);
    if ( column < styles.length) System.arraycopy( styles, column, newStyles, column+1, styles.length - column);
    styles = newStyles;
    
    int[] newWidest = new int[ widest.length + 1];
    if ( column > 0) System.arraycopy( widest, 0, newWidest, 0, column);
    if ( column < widest.length) System.arraycopy( widest, column, newWidest, column+1, widest.length - column);
    widest = newWidest;
    
    List<Integer>[] newSorted = new List[ sorted.length + 1];
    if ( column > 0) System.arraycopy( sorted, 0, newSorted, 0, column);
    if ( column < sorted.length) System.arraycopy( sorted, column, newSorted, column+1, sorted.length - column);
    sorted = newSorted;
    
    int[] newSizes = new int[ sizes.length + 1];
    if ( column > 0) System.arraycopy( sizes, 0, newSizes, 0, column);
    if ( column < sizes.length) System.arraycopy( sizes, column, newSizes, column+1, sizes.length - column);
    sizes = newSizes;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#removeColumn(int)
   */
  public final void removeColumn( int column)
  {
    ColumnStyle[] newStyles = new ColumnStyle[ styles.length - 1];
    if ( column > 0) System.arraycopy( styles, 0, newStyles, 0, column);
    if ( column < styles.length) System.arraycopy( styles, column+1, newStyles, column, styles.length - 1 - column);
    styles = newStyles;
    
    int[] newWidest = new int[ widest.length - 1];
    if ( column > 0) System.arraycopy( widest, 0, newWidest, 0, column);
    if ( column < widest.length) System.arraycopy( widest, column+1, newWidest, column, widest.length - 1 - column);
    widest = newWidest;
    
    List<Integer>[] newSorted = new List[ sorted.length - 1];
    if ( column > 0) System.arraycopy( sorted, 0, newSorted, 0, column);
    if ( column < sorted.length) System.arraycopy( sorted, column+1, newSorted, column, sorted.length - 1 - column);
    sorted = newSorted;
    
    int[] newSizes = new int[ sizes.length - 1];
    if ( column > 0) System.arraycopy( sizes, 0, newSizes, 0, column);
    if ( column < sizes.length) System.arraycopy( sizes, column+1, newSizes, column, sizes.length - 1 - column);
    sizes = newSizes;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setAbsoluteWidth(int, int, boolean)
   */
  public final void setAbsoluteWidth( int column, int width, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.absolute;
    
    style.minimum = width;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.minimum *= charWidth;
    }
    
    style.maximum = style.minimum;
    styles[ column] = style;
    
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setFreeWidth(int)
   */
  public final void setFreeWidth( int column)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.free;
    styles[ column] = style;
    
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setRelativeWidth(int, int, double, boolean)
   */
  public final void setRelativeWidth( int column, int maximum, double relative, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.relative;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.maximum *= charWidth;
    }
    style.relative = relative;
    styles[ column] = style;
    
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setAutoWidth(int, int, int, boolean)
   */
  public final void setAutoWidth( int column, int minimum, int maximum, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.auto;
    style.minimum = minimum;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.minimum *= charWidth;
      style.maximum *= charWidth;
    }
    styles[ column] = style;
    
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setWidth(int)
   */
  public final void setWidth( int width)
  {
    this.width = width;
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setColumnText(int, int, java.lang.String)
   */
  public final void setColumnText( int row, int column, String text)
  {
    if ( row > rows.size()) insertRow( row);
    
    int width = getWidth( text);
    Integer[] widths = rows.get( row);
    widths[ column] = width;
    
    int index = Collections.binarySearch( sorted[ column], width);
    sorted[ column].add( index, row);
    
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#insertRow(int)
   */
  public final void insertRow( int row)
  {
    for( int i=rows.size(); i<=row; i++)
    {
      Integer[] widths = new Integer[ styles.length];
      rows.add( row, widths);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#removeRow(int)
   */
  public final void removeRow( int row)
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
    
    update();
  }

  /**
   * Returns the width of the specified text.
   * @param text The text.
   * @return Returns the width of the specified text.
   */
  protected abstract int getWidth( String text);
  
  /**
   * Compute the width of each column.
   * @param width Returns the computed width of each column.
   * @param padding The amount of additional space added to each computed width.
   * @return Returns the width of each column.
   */
  private final void compute( int[] widths, int padding)
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
  
  /**
   * Update column sizes and notify subclass.
   */
  private void update()
  {
    int[] newSizes = new int[ sizes.length];
    compute( newSizes, padding);
    
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    for( int i=0; i<newSizes.length; i++)
    {
      if ( newSizes[ i] != sizes[ i])
        feature.setColumnWidth( i, newSizes[ i]);
    }
    
    sizes = newSizes;
  }
  
  private enum Mode { absolute, relative, free, auto};
  
  private final static class ColumnStyle
  {
    public Mode mode;
    public int minimum;
    public int maximum;
    public double relative;
  }

  protected IXidget xidget;
  private ColumnStyle[] styles;
  private List<Integer[]> rows;
  private List<Integer>[] sorted;
  private int[] widest;
  private int[] sizes;
  private int width;
  private int padding;
}
