/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.ArrayList;
import java.util.List;
import org.xmodel.IModelObject;

/**
 * A default implementation of ITableModelFeature.
 */
public class TableModelFeature implements ITableModelFeature
{
  public TableModelFeature()
  {
    this.rows = new ArrayList<List<IModelObject>>();
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#insertRows(int, int)
   */
  public void insertRows( int start, int count)
  {
    for( int i=0; i<count; i++)
      rows.add( start, null);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#removeRows(int, int)
   */
  public void removeRows( int start, int count)
  {
    for( int i=0; i<count; i++)
      rows.remove( start);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setSource(int, int, java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( int row, int column, String channel, IModelObject node)
  {
    List<IModelObject> columns = rows.get( row);
    if ( columns == null) 
    {
      columns = new ArrayList<IModelObject>( 1);
      rows.set( row, columns);
    }
    
    if ( columns.size() <= column)
    {
      for( int i=columns.size(); i<=column; i++)
        columns.add( null);
    }
    
    columns.set( column, node);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getSource(int, int, String)
   */
  public IModelObject getSource( int row, int column, String channel)
  {
    List<IModelObject> columns = rows.get( row);
    if ( columns == null || column >= columns.size()) return null;
    return columns.get( column);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setText(int, int, java.lang.String)
   */
  public void setText( int row, int column, String channel, String text)
  {
    IModelObject node = getSource( row, column, channel);
    if ( node != null) node.setValue( text);
  }
  
  private List<List<IModelObject>> rows;
}
