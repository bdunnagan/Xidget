/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableModelFeature;
import org.xidget.table.Row;
import org.xmodel.IModelObject;

/**
 * A default implementation of ITableModelFeature.
 */
public class TableModelFeature implements ITableModelFeature
{
  public TableModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#getSource(org.xidget.table.Row, int)
   */
  public IModelObject getSource( Row row, int column)
  {
    return row.cells.get( column).source;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.ITableModelFeature#setText(org.xidget.table.Row, int, java.lang.String)
   */
  public void setText( Row row, int column, String text)
  {
    IModelObject node = getSource( row, column);
    if ( node != null) node.setValue( text);
  }

  protected IXidget xidget;
}
