/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.ifeature.table.ITableWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the column title of a table.
 */
public class ColumnTitleBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ITableWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    int column = element.getParent().getChildren( element.getType()).indexOf( element);
    return new Listener( xidget, column);
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, int column)
    {
      this.xidget = xidget;
      this.column = column;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, boolean)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, Boolean.toString( newValue));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, Double.toString( newValue));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, newValue);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, Xlate.get( nodes.get( 0), ""));
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
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, Xlate.get( node, ""));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      ITableWidgetFeature feature = xidget.getFeature( ITableWidgetFeature.class);
      feature.setTitle( column, (newValue != null)? newValue.toString(): "");
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
    private int column;
  }
}
