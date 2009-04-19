/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableModelFeature;
import org.xidget.table.Cell;
import org.xidget.table.Column;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of ITableModelFeature.
 */
public class TableModelFeature implements ITableModelFeature
{
  public TableModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.rows = new ArrayList<Row>();
    this.columns = new ArrayList<Column>( 5);
    
    configure( xidget.getConfig());
  }
  
  /**
   * Configure the columns of the table.
   * @param element The table configuration element.
   */
  private void configure( IModelObject element)
  {
    int index = 0;
    for( IModelObject columnElement: element.getChildren( "column"))
    {
      Column column = new Column();
      column.titleExpr = Xlate.childGet( columnElement, "title", (IExpression)null);
      column.imageExpr = Xlate.childGet( columnElement, "image", (IExpression)null);
      column.sourceExpr = Xlate.childGet( columnElement, "source", (IExpression)null);
      column.titleListener = new ColumnTitleListener( xidget, index++);  
      
      columns.add( column);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#insertRow(int, org.xmodel.xpath.expression.StatefulContext)
   */
  public Row insertRow( int index, StatefulContext context)
  {
    Row row = new Row();
    row.context = context;
    row.cells = new ArrayList<Cell>( 5);
    rows.add( index, row);
    
    for( int i=0; i<columns.size(); i++)
    {
      Column column = columns.get( i);
      
      Cell cell = new Cell();
      cell.imageListener = new ColumnImageListener( xidget, row, i);
      cell.sourceListener = new ColumnSourceListener( xidget, row, i);
      row.cells.add( cell);
      
      if ( column.titleExpr != null) column.titleExpr.addNotifyListener( row.context, column.titleListener);
      if ( column.imageExpr != null) column.imageExpr.addNotifyListener( row.context, cell.imageListener);
      if ( column.sourceExpr != null) column.sourceExpr.addNotifyListener( row.context, cell.sourceListener);
    }
    
    return row;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#removeRow(int)
   */
  public void removeRow( int index)
  {
    Row row = rows.remove( index);
    for( int i=0; i<columns.size(); i++)
    {
      Column column = columns.get( i);
      Cell cell = row.cells.get( i);
      
      if ( column.titleExpr != null) column.titleExpr.removeListener( row.context, column.titleListener);
      if ( column.imageExpr != null) column.imageExpr.removeListener( row.context, cell.imageListener);
      if ( column.sourceExpr != null) column.sourceExpr.removeListener( row.context, cell.sourceListener);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#getRows()
   */
  public List<Row> getRows()
  {
    return rows;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#getColumns()
   */
  public List<Column> getColumns()
  {
    return columns;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#getSource(org.xidget.table.Row, int)
   */
  public IModelObject getSource( Row row, int column)
  {
    return row.cells.get( column).source;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#setText(org.xidget.table.Row, int, java.lang.String)
   */
  public void setText( Row row, int column, String text)
  {
    IModelObject node = getSource( row, column);
    if ( node != null) node.setValue( text);
  }

  private IXidget xidget;
  private List<Column> columns;
  private List<Row> rows;
}
