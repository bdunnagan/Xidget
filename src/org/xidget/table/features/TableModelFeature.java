/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.text.TextXidget;
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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getIcon(int, int)
   */
  public Object getIcon( int row, int column)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getRow(int)
   */
  public IModelObject getRow( int row)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getNode(int, int)
   */
  public IModelObject getNode( int row, int column)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#getText(int, int)
   */
  public String getText( int row, int column)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.ITableModelFeature#setText(int, int, java.lang.String)
   */
  public void setText( int row, int column, String text)
  {
    IModelObject node = getNode( row, column);
    if ( node == null) return;
    
    // process the text
    ITextModelFeature modelFeature = getTextModelFeature( column);
    modelFeature.setSource( TextXidget.allChannel, node);
    modelFeature.setText( TextXidget.allChannel, text);
    
    // clear the node which is no longer needed
    modelFeature.setSource( TextXidget.allChannel, null);
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
}
