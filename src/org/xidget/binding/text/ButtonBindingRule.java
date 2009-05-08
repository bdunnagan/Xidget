/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding.text;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.ifeature.button.IButtonModelFeature;
import org.xidget.ifeature.button.IButtonWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the state of a button widget.
 */
public class ButtonBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IButtonModelFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IButtonModelFeature modelAdapter = xidget.getFeature( IButtonModelFeature.class);
      IModelObject source = expression.queryFirst( context);
      if ( source == modelAdapter.getSource()) return;      
      
      modelAdapter.setSource( source);
      
      IButtonWidgetFeature widgetAdapter = xidget.getFeature( IButtonWidgetFeature.class);
      widgetAdapter.setState( Xlate.get( source, false));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IButtonModelFeature modelAdapter = xidget.getFeature( IButtonModelFeature.class);
      IModelObject source = expression.queryFirst( context);
      if ( source == modelAdapter.getSource()) return;
      
      modelAdapter.setSource( source);
      
      IButtonWidgetFeature widgetAdapter = xidget.getFeature( IButtonWidgetFeature.class);
      widgetAdapter.setState( Xlate.get( source, false));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      IButtonWidgetFeature widgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      widgetFeature.setState( newValue);
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      IButtonWidgetFeature widgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
      boolean newState = (newValue != 0);
      boolean oldState = (oldValue != 0);
      if ( newState != oldState) widgetFeature.setState( newState);
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      try
      {
        IButtonWidgetFeature widgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
        boolean newState = Boolean.parseBoolean( newValue);
        boolean oldState = Boolean.parseBoolean( oldValue);
        if ( newState != oldState) widgetFeature.setState( newState);
      }
      catch( Exception e)
      {
      }
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      try
      {
        IButtonWidgetFeature widgetFeature = xidget.getFeature( IButtonWidgetFeature.class);
        boolean newState = Boolean.parseBoolean( newValue.toString());
        boolean oldState = Boolean.parseBoolean( oldValue.toString());
        if ( newState != oldState) widgetFeature.setState( newState);
      }
      catch( Exception e)
      {
      }
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
  }
}
