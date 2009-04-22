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
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ExpressionListener which updates the source of a table cell.
 */
public class ColumnSourceListener extends ExpressionListener
{
  public ColumnSourceListener( IXidget xidget, Row row, int columnIndex)
  {
    this.xidget = xidget;
    this.row = row;
    this.columnIndex = columnIndex;
  }

  /**
   * Set the text of the cell.
   * @param text The text.
   */
  private void setText( String text)
  {
    row.cells.get( columnIndex).text = text;
    ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /**
   * Set the source of the cell.
   * @param source The source node.
   */
  private void setSource( IModelObject source)
  {
    row.cells.get( columnIndex).source = source;
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, boolean)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, boolean newValue)
  {
    setText( Boolean.toString( newValue));
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, double, double)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
  {
    setText( Double.toString( newValue));
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
  {
    setText( newValue);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes.clear(); expression.query( context, nodes);

    setText( Xlate.get( nodes.get( 0), ""));
    setSource( nodes.get( 0));    
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes.clear(); expression.query( context, nodes);
  
    IModelObject node = (nodes.size() > 0)? nodes.get( 0): null;
    setText( Xlate.get( node, ""));
    setSource( node);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    setText( (newValue != null)? newValue.toString(): "");
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
