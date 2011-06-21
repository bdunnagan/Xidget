/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.tree.IColumnWidthFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xmodel.BreadthFirstIterator;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IColumnWidthFeature that provides a very fast and flexible
 * algorithm for computing the width of the columns of a tree, table or other 
 * columnar xidget.
 */
@SuppressWarnings("unchecked")
public abstract class ColumnWidthFeature implements IColumnWidthFeature
{
  protected ColumnWidthFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.rows = new ArrayList<Integer[]>();
  }

  /**
   * Extract the column children of the specified xidget configuration and configure
   * this feature from the width child of each column. The content of the width child
   * is one of the following:
   * <ul>
   * <li>A positive integer for absolute width.
   * <li>A positive real followed by a "%" sign for relative width.
   * <li>The word "auto" for automatic sizing.
   * </ul>
   * <p>
   * The presence of a "c" on the end of a value indicates that the value is specified
   * in characters as opposed to pixels.
   * <p>
   * Automatic sizing supports a minimium and maximum width, which are specified in 
   * the "min" and "max" attributes of the width element.
   * <p>
   * Relative sizing supports a maximum width, which is specified in the "max" attribute.
   * <p>
   * Finally, extra space may be added to a column using the "pad" attribute. 
   * @param xidget The xidget.
   */
  public void configure( IXidget xidget)
  {
    int count = findNumberOfColumns( xidget);
    styles = new ColumnStyle[ count];
    widest = new int[ count];
    sizes = new int[ count];
    sorted = new List[ count];
    for( int i=0; i<count; i++) sorted[ i] = new ArrayList<Integer>();
    
    int charWidth = getWidth( "X");
    
    List<IModelObject> columns = getColumnDeclarations( xidget);
    for( int i=0; i<columns.size(); i++)
    {
      IModelObject column = columns.get( i);
      IModelObject width = column.getFirstChild( "width");
      if ( width == null)
      {
        setFreeWidth( i, -1, -1, 0);
      }
      else
      {
        String padSpec = Xlate.get( width, "pad", (String)null);
        int pad = (padSpec != null)? parseConstraint( padSpec, charWidth): 0;
        
        String minSpec = Xlate.get( width, "min", (String)null);
        int min = (minSpec != null)? parseConstraint( minSpec, charWidth): -1;
        
        String maxSpec = Xlate.get( width, "max", (String)null);
        int max = (maxSpec != null)? parseConstraint( maxSpec, charWidth): -1;

        String spec = Xlate.get( width, "free");
        if ( spec.equals( "free"))
        {
          setFreeWidth( i, min, max, pad);
        }
        else if ( spec.equals( "auto"))
        {
          setAutoWidth( i, min, max, pad, false);
        }
        else if ( spec.endsWith( "%"))
        {
          double relative = Double.parseDouble( spec.substring( 0, spec.length() - 1)) / 100;
          setRelativeWidth( i, min, max, relative, pad, false);
        }
        else
        {
          int absolute = parseConstraint( spec, charWidth);
          setAbsoluteWidth( i, absolute, pad, false);
        }
      }
    }
    
    for( int i=columns.size(); i<count; i++)
    {
      setFreeWidth( i, -1, -1, 0);
    }
  }
  
  private static List<IModelObject> getColumnDeclarations( IXidget xidget)
  {
    IModelObject config = xidget.getConfig();

    // add column declarations
    List<IModelObject> result = new ArrayList<IModelObject>();
    result.addAll( config.getChildren( "column"));
    
    // add cell declarations if they define a title or a width
    List<IModelObject> cells = config.getChildren( "cell");
    for( int i=0; i<cells.size(); i++)
    {
      IModelObject cell = cells.get( i);
      if ( i == result.size()) result.add( cell);
    }
    
    return result;
  }
  
  /**
   * It is not required that a xidget declare a column.  Columns are automatically created for sub-tables
   * that define additional cells.  This method calculates the number of columns from the sub-table with
   * the most cell declarations.
   * @param xidget The xidget.
   * @return Returns the number of maximum number of cells defined in any table of the specified xidget.
   */
  private static int findNumberOfColumns( IXidget xidget)
  {
    int columns = 0;
    IModelObject config = xidget.getConfig();
    BreadthFirstIterator iter = new BreadthFirstIterator( config);
    while( iter.hasNext())
    {
      IModelObject node = iter.next();
      if ( node.isType( "table"))
      {
        int cells = node.getNumberOfChildren( "cell");
        if ( cells > columns) columns = cells;
      }
    }
    return columns;
  }
  
  /**
   * Parse a minimum or maximum constraint with optional suffix "c" indicating that the
   * constraint is measured in characters instead of pixels. Contraints may also end with
   * the suffix "p" even though pixels measurements are the default.
   * @param constraint The constraint specification.
   * @return Returns the constraint in pixels.
   */
  private static int parseConstraint( String constraint, int charWidth)
  {
    char c = constraint.charAt( constraint.length() - 1);
    if ( c == 'c' || c == 'C')
    {
      constraint = constraint.substring( 0, constraint.length() - 1);
      return Integer.parseInt( constraint) * charWidth;
    }
    else if ( c == 'p' || c == 'P')
    {
      constraint = constraint.substring( 0, constraint.length() - 1);
      return Integer.parseInt( constraint);
    }
    else
    {
      return Integer.parseInt( constraint);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#insertColumn(int)
   */
  public final void insertColumn( int column)
  {
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#removeColumn(int)
   */
  public final void removeColumn( int column)
  {
    update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setAbsoluteWidth(int, int, boolean)
   */
  public final void setAbsoluteWidth( int column, int width, int padding, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.absolute;
    style.padding = padding;
    
    style.minimum = width;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.minimum *= charWidth;
    }
    
    style.maximum = style.minimum;
    styles[ column] = style;
    
    //update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setFreeWidth(int)
   */
  public final void setFreeWidth( int column, int minimum, int maximum, int padding)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.free;
    style.minimum = minimum;
    style.maximum = maximum;    
    style.padding = padding;
    styles[ column] = style;
    
    //update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setRelativeWidth(int, int, int, double, int, boolean)
   */
  public final void setRelativeWidth( int column, int minimum, int maximum, double relative, int padding, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.relative;
    style.padding = padding;
    style.minimum = minimum;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.minimum *= charWidth;
      style.maximum *= charWidth;
    }
    style.relative = relative;
    styles[ column] = style;
    
    //update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.IColumnWidthFeature#setAutoWidth(int, int, int, boolean)
   */
  public final void setAutoWidth( int column, int minimum, int maximum, int padding, boolean chars)
  {
    ColumnStyle style = new ColumnStyle();
    style.mode = Mode.auto;
    style.padding = padding;
    style.minimum = minimum;
    style.maximum = maximum;
    if ( chars)
    {
      int charWidth = getWidth( "X");
      style.minimum *= charWidth;
      style.maximum *= charWidth;
    }
    styles[ column] = style;
    
    //update();
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

    if ( widest[ column] < width) widest[ column] = width;
    
    int index = Collections.binarySearch( sorted[ column], width);
    if ( index < 0) index = -(index + 1);
    sorted[ column].add( index, width);
    
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
   * @return Returns the width of each column.
   */
  private final void compute( int[] widths)
  {
    double total = width;
    int columns = styles.length;
    
    // subtract absolute and auto widths from total width
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.absolute)
      {
        widths[ i] = styles[ i].minimum + styles[ i].padding;
        total -= widths[ i];
        columns--;
      }
      else if ( styles[ i].mode == Mode.auto)
      {
        widths[ i] = constrain( styles[ i], widest[ i] + styles[ i].padding);
        total -= widths[ i];
        columns--;
      }
    }
    
    // subtract relative widths in order from total width
    double basis = total;
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.relative)
      {
        widths[ i] = constrain( styles[ i], basis * styles[ i].relative + styles[ i].padding);
        if ( widths[ i] > total) widths[ i] = (int)total;
        total -= widths[ i];
        columns--;
      }
    }
    
    // sum remaining padding
    int padding = 0;
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.free)
      {
        padding += styles[ i].padding;
      }
    }
    
    // distribute remaining space
    int width = (int)Math.floor( total / columns) - padding;
    for( int i=0; i<widths.length; i++)
    {
      if ( styles[ i].mode == Mode.free)
      {
        widths[ i] = constrain( styles[ i], width + styles[ i].padding);
      }
    }
  }
  
  /**
   * Applies the constraints of the specified style.
   * @param style The column sizing style.
   * @param width The raw column width.
   * @return Returns the constrained column width.
   */
  private static int constrain( ColumnStyle style, double width)
  {
    if ( style.minimum >= 0 && width < style.minimum) 
    {
      return style.minimum;
    }
    else if ( style.maximum >= 0 && width > style.maximum) 
    {
      return style.maximum;
    }
    return (int)Math.round( width);
  }
  
  /**
   * Update column sizes and notify subclass.
   */
  private void update()
  {
    int[] newSizes = new int[ sizes.length];
    compute( newSizes);
    
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    for( int i=0; i<newSizes.length; i++)
    {
      //if ( newSizes[ i] != sizes[ i])
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
    public int padding;
  }
  
  protected IXidget xidget;
  private ColumnStyle[] styles;
  private List<Integer[]> rows;
  private List<Integer>[] sorted;
  private int[] widest;
  private int[] sizes;
  private int width;
}
