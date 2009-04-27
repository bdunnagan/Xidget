/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.table.ColumnSetFeature;
import org.xidget.feature.table.GroupOffsetFeature;
import org.xidget.feature.table.RowSetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.table.IColumnSetFeature;
import org.xidget.ifeature.table.IGroupOffsetFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;

/**
 * A xidget representing a contiguous set of rows in the table.
 */
public class SubTableXidget extends Xidget
{
  public void createFeatures()
  {
    rowSetFeature = new RowSetFeature( this);
    columnSetFeature = new ColumnSetFeature( this);
    bindFeature = new BindFeature( this);
    groupFeature = new GroupOffsetFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IGroupOffsetFeature.class) return (T)groupFeature;
    if ( clss == IColumnSetFeature.class) return (T)columnSetFeature;
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;

    // get features from parent
    if ( clss == ITableWidgetFeature.class) return (T)getParent().getFeature( clss);
    
    return super.getFeature( clss);
  }
  
  private IRowSetFeature rowSetFeature;
  private IColumnSetFeature columnSetFeature;
  private IBindFeature bindFeature;
  private IGroupOffsetFeature groupFeature;
}
