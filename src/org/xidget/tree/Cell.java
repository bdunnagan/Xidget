/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tree;

import org.xidget.feature.tree.ColumnImageListener;
import org.xidget.feature.tree.ColumnSourceListener;
import org.xmodel.IModelObject;

/**
 * A data-structure for holding information about a table cell.
 */
public class Cell
{
  public Object icon;
  public String text;
  public IModelObject source;
  public ColumnImageListener imageListener;
  public ColumnSourceListener sourceListener;
}
