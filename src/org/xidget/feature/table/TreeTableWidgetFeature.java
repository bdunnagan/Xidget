/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITableWidgetFeature for use with a sub-tree xidget which 
 * binds its sub-table xidgets to each row of its parent tree.
 */
public class TreeTableWidgetFeature implements ITableWidgetFeature, ITreeExpandFeature
{
  public TreeTableWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#insertRows(org.xmodel.xpath.expression.StatefulContext, int, org.xidget.table.Row[])
   */
  public void insertRows( StatefulContext parent, int rowIndex, Row[] rows)
  {
    // update model
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    Row row = feature.findRow( parent);
    if ( row != null)
    {
      for( int i=0; i<rows.length; i++) 
        row.addChild( rowIndex + i, rows[ i]);
    }
    
    // update tree widget
    feature.insertRows( row, rowIndex, rows);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#removeRows(org.xmodel.xpath.expression.StatefulContext, int, org.xidget.table.Row[])
   */
  public void removeRows( StatefulContext parent, int rowIndex, Row[] rows)
  {
    // update model
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    Row row = rows[ 0].getParent();
    if ( row != null)
    {
      for( int i=0; i<rows.length; i++) 
        row.removeChild( rowIndex);
    }
    
    // update tree widget
    feature.removeRows( row, rowIndex, rows);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#getRows(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<Row> getRows( StatefulContext parent)
  {
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    Row row = feature.findRow( parent);
    return row.getChildren();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setEditable(int, int, boolean)
   */
  public void setEditable( int rowIndex, int columnIndex, boolean editable)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#setTitle(int, java.lang.String)
   */
  public void setTitle( int columnIndex, String title)
  {
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.setTitle( columnIndex, title);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#updateCell(org.xidget.table.Row, int)
   */
  public void updateCell( Row row, int columnIndex)
  {
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#expand(org.xidget.table.Row)
   */
  public void expand( Row row)
  {
    // bind appropriate tree xidget
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.tree.ITreeExpandFeature#collapse(org.xidget.table.Row)
   */
  public void collapse( Row row)
  {
    // unbind appropriate tree xidget
  }

  private IXidget xidget;
}
