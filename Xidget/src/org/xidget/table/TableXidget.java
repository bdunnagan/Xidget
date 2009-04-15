/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.AbstractXidget;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.table.features.IRowSetFeature;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
import org.xidget.table.features.RowSetFeature;
import org.xidget.table.features.TableModelFeature;

/**
 * An implementation of IXidget for use with any widget which can display
 * tabular data (including tables with only one column and no column header,
 * i.e. lists).
 * <p>
 * The children of a TableXidget are instances of ColumnXidget.
 */
public abstract class TableXidget extends AbstractXidget
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    
    widgetFeature = getWidgetFeature();
    tableModelFeature = getTableModelFeature();
    tableWidgetFeature = getTableWidgetFeature();
    errorFeature = getErrorFeature();
    rowSetFeature = new RowSetFeature( this);
  }

  /**
   * Returns the required IWidgetFeature.
   * @return Returns the required IWidgetFeature.
   */
  protected abstract IWidgetFeature getWidgetFeature();
  
  /**
   * Returns an implementation of ITableModelFeature. By default, returns TableModelFeature.
   * @return Returns an implementation of ITableModelFeature.
   */
  protected ITableModelFeature getTableModelFeature()
  {
    return new TableModelFeature();
  }
  
  /**
   * Returns the required IWidgetTextFeature.
   * @return Returns the required IWidgetTextFeature.
   */
  protected abstract ITableWidgetFeature getTableWidgetFeature();
  
  /**
   * Returns the required IErrorFeature.
   * @return Returns the required IErrorFeature.
   */
  protected abstract IErrorFeature getErrorFeature();
  
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == ITableModelFeature.class) return (T)tableModelFeature;
    if ( clss == ITableWidgetFeature.class) return (T)tableWidgetFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;    
    
    return super.getFeature( clss);
  }

  private IWidgetFeature widgetFeature;
  private ITableModelFeature tableModelFeature;
  private ITableWidgetFeature tableWidgetFeature;
  private IRowSetFeature rowSetFeature;
  private IErrorFeature errorFeature;
}
