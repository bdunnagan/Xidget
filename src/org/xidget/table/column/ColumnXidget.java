/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column;

import org.xidget.Xidget;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.feature.IIconFeature;
import org.xidget.feature.ITitleFeature;
import org.xidget.table.features.CellTextModelFeature;
import org.xidget.table.features.IRowSetFeature;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.ITextWidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget which represents a table column.
 */
public abstract class ColumnXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    // add to row-set
    IRowSetFeature rowSetFeature = parent.getFeature( IRowSetFeature.class);
    rowSetFeature.addColumn( this);
    
    return true;
  }  

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    
    titleFeature = getTitleFeature();
    iconFeature = getIconFeature();
  }

  /**
   * Returns the required ITitleFeature. The implementation can get the column index
   * from the configuration element of the column xidget.
   * @return Returns the required ITitleFeature.
   */
  protected abstract ITitleFeature getTitleFeature();
  
  /**
   * Returns the required IIconFeature. The implementation can get the column index
   * from the configuration element of the column xidget, and the row index from the
   * IRowSetFeature of the table xidget.
   * @return Returns the required IIconFeature.
   */
  protected abstract IIconFeature getIconFeature();
  
  /**
   * Returns the required ITextWidgetFeature for table cells in this column.
   * @return Returns the required ITextWidgetFeature for table cells in this column.
   */
  protected abstract ITextWidgetFeature getTextWidgetFeature();
  
  /**
   * Returns the required ITextModelFeature for table cells in this column.
   * @return Returns the required ITextModelFeature for table cells in this column.
   */
  protected ITextModelFeature getTextModelFeature()
  {
    return new CellTextModelFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == ITitleFeature.class) return (T)titleFeature;
    if ( clss == IIconFeature.class) return (T)iconFeature;
    
    return super.getFeature( clss);
  }

  private ITitleFeature titleFeature;
  private IIconFeature iconFeature;
}
