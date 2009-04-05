/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.table.column.feature.IColumnBindingFeature;
import org.xmodel.IModelObject;
import org.xmodel.diff.AbstractListDiffer;

/**
 * A default implementation of IRowSetFeature which uses a shallow differ to perform
 * targeted updates to the table xidget to which it belongs. No other feature should
 * make changes to the ITableModelFeature, ITableWidgetFeature or IColumnBindingFeature.
 */
public class RowSetFeature implements IRowSetFeature
{
  public RowSetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.differ = new Differ();
    this.changes = new ArrayList<Change>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.IRowSetFeature#setRows(java.util.List)
   */
  public void setRows( List<IModelObject> newRows)
  {
    ITableModelFeature modelFeature = xidget.getFeature( ITableModelFeature.class);
    List<IColumnBindingFeature> columnBindingFeatures = getColumnBindingFeatures();
    
    // find changes
    changes.clear();
    List<IModelObject> oldRows = modelFeature.getRows();
    differ.diff( oldRows, newRows);
    
    // process changes
    for( Change change: changes)
    {
      if ( change.rIndex >= 0)
      {
        for( int i=0; i<change.count; i++)
          for( IColumnBindingFeature columnBindingFeature: columnBindingFeatures)
            columnBindingFeature.bind( change.lIndex+i, newRows.get( change.rIndex+i));
      }
      else
      {
        for( int i=0; i<change.count; i++)
          for( IColumnBindingFeature columnBindingFeature: columnBindingFeatures)
            columnBindingFeature.unbind( change.lIndex, oldRows.get( change.lIndex));
      }
    }
    
    // update model
    modelFeature.setRows( newRows);
  }

  /**
   * Returns the IColumnBindingFeatures of the column xidgets.
   * @return Returns the IColumnBindingFeatures of the column xidgets.
   */
  private List<IColumnBindingFeature> getColumnBindingFeatures()
  {
    if ( columnBindingFeatures != null) return columnBindingFeatures;
    columnBindingFeatures = new ArrayList<IColumnBindingFeature>();
    for( IXidget child: xidget.getChildren())
    {
      IColumnBindingFeature columnBindingFeature = child.getFeature( IColumnBindingFeature.class);
      if ( columnBindingFeature != null) columnBindingFeatures.add( columnBindingFeature);
    }
    return columnBindingFeatures;
  }
  
  /**
   * An implementation of AbstractListDiffer which calls the createInsertChange
   * and createDeleteChange methods.
   */
  @SuppressWarnings("unchecked")
  private class Differ extends AbstractListDiffer
  {
    /* (non-Javadoc)
     * @see org.xmodel.diff.IListDiffer#notifyInsert(java.util.List, int, int, java.util.List, int, int)
     */
    public void notifyInsert( List lhs, int lIndex, int lAdjust, List rhs, int rIndex, int count)
    {
      Change change = new Change();
      change.lIndex = lIndex + lAdjust;
      change.rIndex = rIndex;
      change.count = count;
      changes.add( change);
    }

    /* (non-Javadoc)
     * @see org.xmodel.diff.IListDiffer#notifyRemove(java.util.List, int, int, java.util.List, int)
     */
    public void notifyRemove( List lhs, int lIndex, int lAdjust, List rhs, int count)
    {
      Change change = new Change();
      change.lIndex = lIndex + lAdjust;
      change.rIndex = -1;
      change.count = count;
      changes.add( change);
    }
  }
  
  private class Change
  {
    public int lIndex;
    public int rIndex;
    public int count;
  }
  
  private IXidget xidget;
  private List<IColumnBindingFeature> columnBindingFeatures;
  private Differ differ;
  private List<Change> changes;
  
}
