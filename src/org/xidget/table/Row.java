/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

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
   * Create a row belonging to the specified table xidget.
   * @param xidget The table xidget.
   */
  public Row( IXidget xidget)
  {
    this.table = xidget;
    this.cells = new ArrayList<Cell>( 1);
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
   * @param index The index where the child will be inserted.
   * @param child The child.
   */
  public void addChild( int index, Row child)
  {
    if ( children == null) children = new ArrayList<Row>();
    children.add( index, child);
    child.parent = this;
  }

  /**
   * Remove the child at the specified index.
   * @param index The index.
   * @return Returns the child that was removed.
   */
  public Row removeChild( int index)
  {
    Row child = children.remove( index);
    child.parent = null;
    return child;
  }
  
  /**
   * Returns the children.
   * @return Returns the children.
   */
  public List<Row> getChildren()
  {
    if ( children == null) return Collections.emptyList();
    return children;
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
  
  private IXidget table;
  private Row parent;
  private List<Row> children;
  private StatefulContext context;
  private List<Cell> cells;
  private XidgetSwitch treeSwitch;
  private boolean expanded;
}
