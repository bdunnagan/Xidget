/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.table.features.IRowSetFeature;
import org.xidget.table.features.ITableModelFeature;
import org.xidget.table.features.ITableWidgetFeature;
import org.xidget.table.features.RowSetFeature;
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
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    // get features
    widgetFeature = getWidgetFeature();
    tableModelFeature = getTableModelFeature();
    tableWidgetFeature = getTableWidgetFeature();
    errorFeature = getErrorFeature();
    rowSetFeature = new RowSetFeature( this);
    
    return true;
  }  

  /**
   * Returns the required IWidgetFeature.
   * @return Returns the required IWidgetFeature.
   */
  protected abstract IWidgetFeature getWidgetFeature();
  
  /**
   * Returns an implementation of ITableModelFeature.
   * @return Returns an implementation of ITableModelFeature.
   */
  protected abstract ITableModelFeature getTableModelFeature();
  
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
   * @see org.xidget.AbstractXidget#setFeature(java.lang.Class, java.lang.Object)
   */
  @Override
  public void setFeature( Class<? extends Object> featureClass, Object feature)
  {
    if ( featureClass == IRowSetFeature.class) rowSetFeature = (IRowSetFeature)feature;
    else super.setFeature( featureClass, feature);
  }

  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == ITableModelFeature.class) return (T)tableModelFeature;
    if ( clss.equals( ITableWidgetFeature.class)) return (T)tableWidgetFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;    
    return super.getFeature( clss);
  }

  private IWidgetFeature widgetFeature;
  private ITableModelFeature tableModelFeature;
  private ITableWidgetFeature tableWidgetFeature;
  private IRowSetFeature rowSetFeature;
  private IErrorFeature errorFeature;
}
