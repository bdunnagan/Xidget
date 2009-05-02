/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.List;
import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A final implementation of RowSetFeature which stores its rows locally.
 */
public class TableRowSetFeature extends RowSetFeature
{
  public TableRowSetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.table.RowSetFeature#setRows(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  @Override
  public void setRows( StatefulContext context, List<IModelObject> nodes)
  {
    super.setRows( context, nodes);
    rows = nodes;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRows(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<IModelObject> getRows( StatefulContext context)
  {
    return rows;
  }

  private List<IModelObject> rows;
}
