/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.table.column.CellXidget;
import org.xidget.table.column.ColumnXidget;
import org.xidget.table.features.IRowSetFeature;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
import org.xidget.table.features.RowSetFeature;
import org.xidget.table.features.TableModelFeature;
import org.xmodel.IModelObject;

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
   * @see org.xidget.AbstractXidget#startConfig(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    // both the header and the cell xidgets are configured from the column element
    columnTagHandler = new XidgetTagHandler( ColumnXidget.class);
    cellTagHandler = new XidgetTagHandler( CellXidget.class);
    
    processor.addHandler( "column", columnTagHandler);
    processor.addHandler( "column", cellTagHandler);
    
    // configure
    return super.startConfig( processor, parent, element);
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  @Override
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    super.endConfig( processor, element);
    
    // both the header and the cell xidgets are configured from the column element
    processor.removeHandler( "column", columnTagHandler);
    processor.removeHandler( "column", cellTagHandler);
  }

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
  private XidgetTagHandler columnTagHandler;
  private XidgetTagHandler cellTagHandler;
}
