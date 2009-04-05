/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.text.feature.ITextModelFeature;
import org.xmodel.IModelObject;

/**
 * A default implementation of ITableModelFeature.
 */
public class TableModelFeature implements ITableModelFeature
{
  /**
   * Create a TableModelFeature for the specified xidget.
   * @param xidget The xidget.
   */
  public TableModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.rows = Collections.<IModelObject>emptyList();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getRows()
   */
  public List<IModelObject> getRows()
  {
    return rows;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setRows(java.util.List)
   */
  public void setRows( List<IModelObject> rows)
  {
    this.rows = rows;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setSource(int, int, java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( int row, int column, String channel, IModelObject node)
  {
    IXidget xidget = getColumnXidget( column);
    if ( xidget != null)
    {
      ITextModelFeature modelFeature = xidget.getFeature( ITextModelFeature.class);
      modelFeature.setSource( channel, node);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getSource(int, int, String)
   */
  public IModelObject getSource( int row, int column, String channel)
  {
    IXidget xidget = getColumnXidget( column);
    if ( xidget != null)
    {
      ITextModelFeature modelFeature = xidget.getFeature( ITextModelFeature.class);
      return modelFeature.getSource( channel);
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setText(int, int, java.lang.String)
   */
  public void setText( int row, int column, String channel, String text)
  {
    IModelObject node = getSource( row, column, channel);
    if ( node == null) return;
    
    // process the text
    ITextModelFeature modelFeature = getTextModelFeature( column);
    modelFeature.setSource( channel, node);
    modelFeature.setText( channel, text);
    
    // clear the node which is no longer needed
    modelFeature.setSource( channel, null);
  }
  
  /**
   * Returns the column xidget for the specified column.
   * @param column The column index.
   * @return Returns null or the column xidget for the specified column.
   */
  private IXidget getColumnXidget( int column)
  {
    List<IXidget> children = xidget.getChildren();
    if ( children.size() > column) return children.get( column);
    return null;
  }
  
  /**
   * Returns the ITextModelFeature for the specified column.
   * @param column The column.
   * @return Returns the ITextModelFeature for the specified column.
   */
  private ITextModelFeature getTextModelFeature( int column)
  {
    List<IXidget> children = xidget.getChildren();
    return children.get( column).getFeature( ITextModelFeature.class);
  }
  
  private IXidget xidget;
  private List<IModelObject> rows;
}
