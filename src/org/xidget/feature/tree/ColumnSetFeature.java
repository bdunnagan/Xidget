/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ColumnSetFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
      column.imageExpr = Xlate.get( columnElement, "image", Xlate.childGet( columnElement, "image", (IExpression)null));
      column.sourceExpr = Xlate.get( columnElement, "source", Xlate.childGet( columnElement, "source", (IExpression)null));
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
