/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.IColumnSetFeature;
import org.xidget.ifeature.table.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
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
    this.tableIndex = findTableIndex( xidget);
  }
  
  /**
   * Returns the table index of the specified table xidget.
   * @param xidget The table xidget.
   * @return Returns the table index of the specified table xidget.
   */
  private int findTableIndex( IXidget xidget)
  {
    int index = 0;
    IXidget parent = xidget.getParent();
    for( IXidget child: parent.getChildren())
    {
      if ( child.getConfig().isType( xidget.getConfig().getType()))
      {
        if ( child == xidget) return index;
        index++;
      }
    }
    return -1;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.IRowSetFeature#setRows(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  public void setRows( StatefulContext context, List<IModelObject> nodes)
  {
    // find parent row
    ITreeWidgetFeature widgetFeature = xidget.getFeature( ITreeWidgetFeature.class);
    Row parent = widgetFeature.findRow( context); 
    
    // get the children of the table to which this row-set belongs
    List<Row> children = parent.getChildren( tableIndex);
    int offset = parent.getOffset( tableIndex);
    
    // find changes
    Differ differ = new Differ();
    differ.diff( toElements( children), nodes);
    List<Change> changes = differ.getChanges();
    
    // process changes
    IColumnSetFeature columnSetFeature = xidget.getFeature( IColumnSetFeature.class);
    for( Change change: changes)
    {
      if ( change.rIndex >= 0)
      {
        // create rows
        Row[] inserted = new Row[ change.count];
        for( int i=0; i<change.count; i++)
        {
          // create row context
          IModelObject rowObject = nodes.get( change.rIndex + i);
          
          // create row
          Row row = new Row( xidget);
          row.setContext( new StatefulContext( context, rowObject));
          parent.addChild( tableIndex, change.lIndex + i, row);
          inserted[ i] = row;
        }        

        // insert rows
        widgetFeature.insertRows( parent, change.lIndex + offset, inserted);

        // update columns of each inserted row
        for( int i=0; i<change.count; i++)
        {
          // bind column set
          Row row = inserted[ i]; 
          columnSetFeature.bind( row, row.getContext());
        }
      }
      else
      {
        // current row doesn't change while deleting
        Row[] deleted = new Row[ change.count];
        for( int i=0; i<change.count; i++)
        {
          // unbind column set
          deleted[ i] = children.get( change.lIndex);
          columnSetFeature.unbind( deleted[ i]);
          parent.removeChild( tableIndex, change.lIndex);
        }
        
        // remove rows
        widgetFeature.removeRows( parent, change.lIndex + offset, deleted);
      }
    }
    
    // commit changes
    widgetFeature.commit( parent);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRows(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<IModelObject> getRows( StatefulContext context)
  {
    ITreeWidgetFeature widgetFeature = xidget.getFeature( ITreeWidgetFeature.class);
    Row parent = widgetFeature.findRow( context); 
    return toElements( parent.getChildren());
  }

  /**
   * Create a list of elements from the context objects of the specified rows.
   * @param rows The rows.
   * @return Returns a list of elements.
   */
  private List<IModelObject> toElements( List<Row> rows)
  {
    List<IModelObject> elements = new ArrayList<IModelObject>( rows.size());
    for( Row row: rows) elements.add( row.getContext().getObject());
    return elements;
  }
  
  /**
   * An implementation of AbstractListDiffer which calls the createInsertChange
   * and createDeleteChange methods.
   */
  @SuppressWarnings("unchecked")
  private static class Differ extends AbstractListDiffer
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
      
      if ( changes == null) changes = new ArrayList<Change>();
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
      
      if ( changes == null) changes = new ArrayList<Change>();
      changes.add( change);
    }
    
    /**
     * Returns the changes.
     * @return Returns the changes.
     */
    public List<Change> getChanges()
    {
      if ( changes == null) return Collections.emptyList();
      return changes;
    }
    
    private List<Change> changes;
  }
  
  private static class Change
  {
    public int lIndex;
    public int rIndex;
    public int count;
  }
  
  protected IXidget xidget;
  private int tableIndex;
}
