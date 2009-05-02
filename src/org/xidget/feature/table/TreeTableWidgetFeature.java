/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.XidgetSwitch;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.table.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITableWidgetFeature for use with a sub-tree xidget which 
 * binds its sub-table xidgets to each row of its parent tree.
 */
public class TreeTableWidgetFeature implements ITableWidgetFeature
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
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    
    // here is where the xpath callback context is translated into the row object
    Row row = feature.findRow( parent);
    if ( row != null)
    {
      for( int i=0; i<rows.length; i++) 
        row.addChild( rowIndex + i, rows[ i]);
    }
    
    // update tree widget
    feature.insertRows( row, rowIndex, rows);
    
    // apply tree expansion policy
    ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
    for( int i=0; i<rows.length; i++)
      expandFeature.rowAdded( rows[ i]);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableWidgetFeature#removeRows(org.xmodel.xpath.expression.StatefulContext, int, org.xidget.table.Row[])
   */
  public void removeRows( StatefulContext parent, int rowIndex, Row[] rows)
  {
    try
    {
      // unbind tree switches where present
      for( int i=0; i<rows.length; i++)
      {
        XidgetSwitch treeSwitch = rows[ i].getSwitch();
        if ( treeSwitch != null) treeSwitch.unbind( rows[ i].getContext());
      }
      
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
    finally
    {
      ITreeExpandFeature expandFeature = xidget.getFeature( ITreeExpandFeature.class);
      for( int i=0; i<rows.length; i++)
        expandFeature.rowRemoved( rows[ i]);
    }
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
  
  private IXidget xidget;
}
