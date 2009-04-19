/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.feature.table.ColumnTitleListener;
import org.xmodel.xpath.expression.IExpression;

/**
 * A data-structure for holding table column information.
 */
public class Column
{
  public String title;
  public IExpression titleExpr;
  public IExpression imageExpr;
  public IExpression sourceExpr;
  public ColumnTitleListener titleListener;
}
