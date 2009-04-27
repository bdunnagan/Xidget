/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ExpressionListener which updates the image of a table cell.
 */
public class ColumnImageListener extends ExpressionListener
{
  public ColumnImageListener( IXidget xidget, Row row, int columnIndex)
  {
    this.xidget = xidget;
    this.row = row;
    this.columnIndex = columnIndex;
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes.clear(); expression.query( context, nodes);
    
    Object value = nodes.get( 0).getValue();
    row.getCell( columnIndex).icon = value;
        
    ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes.clear(); expression.query( context, nodes);
    
    Object value = (nodes.size() > 0)? nodes.get( 0).getValue(): null;
    row.getCell( columnIndex).icon = value;
    
    ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    row.getCell( columnIndex).icon = newValue;
    
    ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
   */
  @Override
  public boolean requiresValueNotification()
  {
    return true;
  }

  private IXidget xidget;
  private Row row;
  private int columnIndex;
}
