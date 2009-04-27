/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A data-structure for the rows of a table or tree xidget.
 */
public class Row
{
  public Row()
  {
    cells = new ArrayList<Cell>( 1);
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
  
  private Row parent;
  private List<Row> children;
  private StatefulContext context;
  private List<Cell> cells;
}
