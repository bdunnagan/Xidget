/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.XidgetSwitch;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A data-structure for the rows of a table or tree xidget.
 */
public class Row
{
  /**
   * Create a row belonging to the specified table. Note that this table information
   * is redundant with respect to the tableIndex argument provided to the add and remove
   * methods.
   * @param table The table xidget.
   */
  public Row( IXidget table)
  {
    this.cells = new ArrayList<Cell>( 1);
    this.tables = new ArrayList<List<Row>>( 1);
    this.table = table;
  }
  
  /**
   * Set the row context.
   * @param context The row context.
   */
  public void setContext( StatefulContext context)
  {
    this.context = context;
  }
  
  /**
   * Returns the row context.
   * @return Returns the row context.
   */
  public StatefulContext getContext()
  {
    return context;
  }
  
  /**
   * Returns the parent.
   * @return Returns null or the parent.
   */
  public Row getParent()
  {
    return parent;
  }
  
  /**
   * Add the specified child at the specified index.
   * @param tableIndex The index of the sub-table.
   * @param rowIndex The index within the sub-table where the child will be inserted.
   * @param child The child.
   */
  public void addChild( int tableIndex, int rowIndex, Row child)
  {
    if ( tableIndex >= tables.size())
      while( tableIndex >= tables.size()) 
        tables.add( null);
    
    List<Row> children = tables.get( tableIndex);
    if ( children == null) 
    {
      children = new ArrayList<Row>();
      tables.set( tableIndex, children);
    }
    
    children.add( rowIndex, child);
    child.parent = this;
  }

  /**
   * Remove the child at the specified index.
   * @param tableIndex The index of the sub-table.
   * @param rowIndex The index within the sub-table of the child to be removed.
   * @return Returns the child that was removed.
   */
  public Row removeChild( int tableIndex, int rowIndex)
  {
    if ( tableIndex >= tables.size()) return null;
    
    List<Row> children = tables.get( tableIndex);
    Row child = children.remove( rowIndex);
    child.parent = null;
    
    return child;
  }

  /**
   * Returns the offset of the first element in the specified table.
   * @param tableIndex The index of the table.
   * @return Returns the offset of the first element in the specified table.
   */
  public int getOffset( int tableIndex)
  {
    int offset = 0;
    for( int i=0; i<tableIndex; i++)
      offset += getChildren( i).size();
    return offset;
  }
  
  /**
   * Returns the children of the specified table.
   * @param tableIndex The index of the table.
   * @return Returns the children of the specified table.
   */
  public List<Row> getChildren( int tableIndex)
  {
    if ( tableIndex < tables.size())
    {
      List<Row> rows = tables.get( tableIndex);
      if ( rows != null) return rows;
    }
    return Collections.emptyList();
  }
  
  /**
   * Returns the children of all tables.
   * @return Returns the children of all tables.
   */
  public List<Row> getChildren()
  {
    List<Row> result = new ArrayList<Row>();
    for( List<Row> children: tables) 
      if ( children != null)
        result.addAll( children);
    return result;
  }
  
  /**
   * Returns the number of children in all tables.
   * @return Returns the number of children in all tables.
   */
  public int getChildCount()
  {
    int count = 0;
    for( List<Row> children: tables) 
      if ( children != null)
        count += children.size();
    return count;
  }
  
  /**
   * Returns the cell with the specified column index. If the cell doesn't exist it is created.
   * @param index The column index.
   * @return Returns the cell with the specified column index.
   */
  public Cell getCell( int index)
  {
    for( int i=cells.size(); i<=index; i++)
      cells.add( new Cell());
    return cells.get( index);
  }
  
  /**
   * Returns the table to which this row belongs.
   * @return Returns the table to which this row belongs.
   */
  public IXidget getTable()
  {
    return table;
  }
  
  /**
   * Set the tree switch for this row.
   * @param treeSwitch The tree switch.
   */
  public void setSwitch( XidgetSwitch treeSwitch)
  {
    this.treeSwitch = treeSwitch;
  }
  
  /**
   * Returns the tree switch for this row.
   * @return Returns null or the tree switch for this row.
   */
  public XidgetSwitch getSwitch()
  {
    return treeSwitch;
  }
  
  /**
   * Set whether the row is expanded.
   * @param expanded True if expanded.
   */
  public void setExpanded( boolean expanded)
  {
    this.expanded = expanded;
  }
  
  /**
   * Returns true if this row is expanded.
   * @return Returns true if this row is expanded.
   */
  public boolean isExpanded()
  {
    return expanded;
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    if ( context != null)
    {
      sb.append( context.getObject().toString());
      sb.append( " [");
      if ( cells != null)
      {
        for( Cell cell: cells)
        {
          sb.append( cell.text);
          sb.append( "|");
        }
      }
      sb.append( "]");
    }
    return sb.toString();
  }
  
  private IXidget table;
  private Row parent;
  private List<List<Row>> tables;
  private StatefulContext context;
  private List<Cell> cells;
  private XidgetSwitch treeSwitch;
  private boolean expanded;
}
