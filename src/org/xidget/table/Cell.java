/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.feature.table.ColumnImageListener;
import org.xidget.feature.table.ColumnSourceListener;
import org.xmodel.IModelObject;

/**
 * A data-structure for holding information about a column in a specific row.
 */
public class Cell
{
  public Object icon;
  public String text;
  public IModelObject source;
  public ColumnImageListener imageListener;
  public ColumnSourceListener sourceListener;
}
