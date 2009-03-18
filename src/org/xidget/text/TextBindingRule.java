/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import java.util.List;
import org.xidget.IBindingRule;
import org.xidget.IXidget;
import org.xidget.text.feature.IModelTextFeature;
import org.xidget.text.feature.IWidgetTextFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the text of a text widget.
 */
public class TextBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget, Xlate.get( element, "channel", TextXidget.allChannel));
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      this.channel = channel;
      modelAdapter = xidget.getFeature( IModelTextFeature.class);
      widgetAdapter = xidget.getFeature( IWidgetTextFeature.class);
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( nodes.contains( modelAdapter.getSource( channel))) return;
      
      IModelObject source = nodes.get( 0);
      modelAdapter.setSource( channel, source);
      widgetAdapter.setText( channel, Xlate.get( source, ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( !nodes.contains( modelAdapter.getSource( channel))) return;
      modelAdapter.setSource( channel, expression.queryFirst( context));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      widgetAdapter.setText( channel, Boolean.toString( newValue));
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      widgetAdapter.setText( channel, Double.toString( newValue));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      widgetAdapter.setText( channel, newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      widgetAdapter.setText( channel, Xlate.get( object, ""));
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private String channel;
    private IModelTextFeature modelAdapter;
    private IWidgetTextFeature widgetAdapter;
  }
}
