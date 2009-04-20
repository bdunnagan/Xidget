/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.feature.table.ColumnTitleListener;
import org.xmodel.xpath.expression.IExpression;

/**
 * A data-structure for holding information about a column header.
 */
public class Header
{
  public String title;
  public IExpression titleExpr;
  public ColumnTitleListener titleListener;
}
