/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.IColumnSetFeature;
import org.xidget.ifeature.tree.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.diff.ListDiffer;
import org.xmodel.diff.ListDiffer.Change;
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
    ListDiffer differ = new ListDiffer();
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
        widgetFeature.removeRows( parent, change.lIndex + offset, deleted, false);
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
    
  protected IXidget xidget;
  private int tableIndex;
}
