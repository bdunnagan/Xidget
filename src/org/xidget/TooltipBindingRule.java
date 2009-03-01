/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>enable</i> configuration element.
 */
public class TooltipBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( (IWidgetAdapter)xidget.getAdapter( IWidgetAdapter.class));
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( IWidgetAdapter adapter)
    {
      this.adapter = adapter;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      adapter.setTooltip( Xlate.get( nodes.get( 0), ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear();
      expression.query( context, nodes);
      if ( nodes.size() > 0) adapter.setTooltip( Xlate.get( nodes.get( 0), "")); 
    }

    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      adapter.setTooltip( newValue);
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      adapter.setTooltip( Xlate.get( object, ""));
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IWidgetAdapter adapter;
  }
}
