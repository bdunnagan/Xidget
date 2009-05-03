/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.IColumnSetFeature;
import org.xidget.tree.Cell;
import org.xidget.tree.Column;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IColumnSetFeature.
 */
public class ColumnSetFeature implements IColumnSetFeature
{
  public ColumnSetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.columns = new ArrayList<Column>( 5);
    configure( xidget.getConfig());
  }
  
  /**
   * Configure the columns of the table.
   * @param element The table configuration element.
   */
  private void configure( IModelObject element)
  {
    for( IModelObject columnElement: element.getChildren( "cell"))
    {
      Column column = new Column();
      column.imageExpr = Xlate.childGet( columnElement, "image", (IExpression)null);
      column.sourceExpr = Xlate.childGet( columnElement, "source", (IExpression)null);
      columns.add( column);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IColumnSetFeature#getColumns()
   */
  public List<Column> getColumns()
  {
    return columns;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IColumnSetFeature#bind(org.xidget.table.Row, org.xmodel.xpath.expression.StatefulContext)
   */
  public void bind( Row row, StatefulContext context)
  {
    for( int i=0; i<columns.size(); i++)
    {
      Column column = columns.get( i);
      
      Cell cell = row.getCell( i);
      cell.imageListener = new ColumnImageListener( xidget, row, i);
      cell.sourceListener = new ColumnSourceListener( xidget, row, i);
      
      if ( column.imageExpr != null) column.imageExpr.addNotifyListener( row.getContext(), cell.imageListener);
      if ( column.sourceExpr != null) column.sourceExpr.addNotifyListener( row.getContext(), cell.sourceListener);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IColumnSetFeature#unbind(org.xidget.table.Row)
   */
  public void unbind( Row row)
  {
    for( int i=0; i<columns.size(); i++)
    {
      Column column = columns.get( i);
      Cell cell = row.getCell( i);
      
      if ( column.imageExpr != null) column.imageExpr.removeListener( row.getContext(), cell.imageListener);
      if ( column.sourceExpr != null) column.sourceExpr.removeListener( row.getContext(), cell.sourceListener);
    }
  }

  protected IXidget xidget;
  private List<Column> columns;
}
