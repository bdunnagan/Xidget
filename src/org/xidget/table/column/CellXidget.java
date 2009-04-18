/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column;

import org.xidget.Xidget;
import org.xidget.feature.IBindFeature;
import org.xidget.feature.IIconFeature;
import org.xidget.table.features.CellTextModelFeature;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.ITextWidgetFeature;

/**
 * An implementation of IXidget which represents a table column.
 */
public class CellXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    
    iconFeature = getIconFeature();
    textModelFeature = getTextModelFeature();
    textWidgetFeature = getTextWidgetFeature();
  }

  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#getBindFeature()
   */
  @Override
  protected IBindFeature getBindFeature()
  {
    // bind feature is set later
    return null;
  }

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
    if ( clss == IIconFeature.class) return (T)iconFeature;
    if ( clss == ITextModelFeature.class) return (T)textModelFeature;
    if ( clss == ITextWidgetFeature.class) return (T)textWidgetFeature;
    
    return super.getFeature( clss);
  }

  private IIconFeature iconFeature;
  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
}
