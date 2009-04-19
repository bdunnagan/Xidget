/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import java.util.List;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A data-structure for a row in a xidget table.
 */
public class Row
{
  public Object handle;
  public StatefulContext context;
  public List<Cell> cells;
}
