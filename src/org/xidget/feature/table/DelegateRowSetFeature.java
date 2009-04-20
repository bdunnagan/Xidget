/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.IGroupOffsetFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IRowSetFeature which pulls its rows from the IRowSetFeatures of its children. 
 */
public class DelegateRowSetFeature implements IRowSetFeature
{
  public DelegateRowSetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#setRows(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  public void setRows( StatefulContext context, List<IModelObject> rows)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRow(int)
   */
  public Row getRow( int index)
  {
    for( IXidget child: xidget.getChildren())
    {
      IRowSetFeature feature = child.getFeature( IRowSetFeature.class);
      if ( feature != null)
      {
        if ( index < feature.getRowCount())
          return feature.getRow( index);
        index -= feature.getRowCount();
      }
    }
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRowCount()
   */
  public int getRowCount()
  {
    int count = 0;
    for( IXidget child: xidget.getChildren())
    {
      IRowSetFeature feature = child.getFeature( IRowSetFeature.class);
      if ( feature != null) count += feature.getRowCount();
    }
    return count;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRowIndex(org.xidget.table.Row)
   */
  public int getRowIndex( Row row)
  {
    for( IXidget child: xidget.getChildren())
    {
      IRowSetFeature feature = child.getFeature( IRowSetFeature.class);
      if ( feature != null)
      {
        int index = feature.getRowIndex( row);
        if ( index >= 0)
        {
          IGroupOffsetFeature offsetFeature = child.getFeature( IGroupOffsetFeature.class);
          return index + offsetFeature.getOffset();
        }
      }
    }
    return -1;
  }
    
  public IXidget xidget;
}
