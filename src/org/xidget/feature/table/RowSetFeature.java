/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.IColumnSetFeature;
import org.xidget.ifeature.table.IGroupOffsetFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.table.Cell;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.diff.AbstractListDiffer;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IRowSetFeature which uses a shallow differ to perform
 * targeted updates to the table xidget to which it belongs. No other feature should
 * make changes to the ITableModelFeature, ITableWidgetFeature or IColumnBindFeature.
 */
public class RowSetFeature implements IRowSetFeature
{
  public RowSetFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.differ = new Differ();
    this.changes = new ArrayList<Change>();
    this.rowObjects = new ArrayList<IModelObject>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.IRowSetFeature#setRows(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  public void setRows( StatefulContext context, List<IModelObject> nodes)
  {
    // find changes
    changes.clear();
    differ.diff( rowObjects, nodes);
    
    // find group offset
    int offset = 0;
    IGroupOffsetFeature offsetFeature = xidget.getFeature( IGroupOffsetFeature.class);
    if ( offsetFeature != null) offset = offsetFeature.getOffset();
    
    // process changes
    IColumnSetFeature columnSetFeature = xidget.getFeature( IColumnSetFeature.class);
    ITableWidgetFeature widgetFeature = xidget.getFeature( ITableWidgetFeature.class);
    for( Change change: changes)
    {
      if ( change.rIndex >= 0)
      {
        // create rows
        Row[] newRows = new Row[ change.count];
        for( int i=0; i<change.count; i++)
        {
          // create row context
          IModelObject rowObject = nodes.get( change.rIndex + i);
          
          // create row
          Row row = new Row();
          row.context = new StatefulContext( context, rowObject);
          row.cells = new ArrayList<Cell>( 5);
          newRows[ i] = row;
        }        
        
        // insert rows
        widgetFeature.insertRows( change.lIndex + offset, newRows);
        
        // update columns of each inserted row
        for( int i=0; i<change.count; i++)
        {
          // bind column set
          Row row = newRows[ i]; 
          columnSetFeature.bind( row, row.context);
        }
      }
      else
      {
        // current row doesn't change while deleting
        List<Row> rows = widgetFeature.getRows();
        for( int i=0; i<change.count; i++)
        {
          // unbind column set
          Row row = rows.get( change.lIndex + offset);
          columnSetFeature.unbind( row);
        }
        
        // remove rows
        widgetFeature.removeRows( change.lIndex + offset, change.count);
      }
    }
    
    // update rows
    rowObjects = nodes;
    rowCount = rowObjects.size();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRowCount()
   */
  public int getRowCount()
  {
    return rowCount;
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
  private Differ differ;
  private List<Change> changes;
  private List<IModelObject> rowObjects;
  private int rowCount;
}
