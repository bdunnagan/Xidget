/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetFeature;
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
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setTooltip( Xlate.get( nodes.get( 0), ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      if ( nodes.size() > 0) 
      {
        IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
        feature.setTooltip( Xlate.get( nodes.get( 0), ""));
      }
    }

    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setTooltip( newValue);
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setTooltip( Xlate.get( object, ""));
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
  }
}
