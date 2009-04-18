/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.text.ifeature.ITextModelFeature;
import org.xidget.text.ifeature.ITextWidgetFeature;
import org.xidget.text.ifeature.TextModelFeature;
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
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ITextModelFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget, Xlate.get( element, "channel", TextModelFeature.allChannel));
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      this.xidget = xidget;
      this.channel = channel;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ITextModelFeature modelAdapter = xidget.getFeature( ITextModelFeature.class);
      if ( nodes.contains( modelAdapter.getSource( channel))) return;
      
      IModelObject source = nodes.get( 0);
      modelAdapter.setSource( channel, source);
      
      ITextWidgetFeature widgetAdapter = xidget.getFeature( ITextWidgetFeature.class);
      widgetAdapter.setText( channel, Xlate.get( source, ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ITextModelFeature modelAdapter = xidget.getFeature( ITextModelFeature.class);
      if ( !nodes.contains( modelAdapter.getSource( channel))) return;
      modelAdapter.setSource( channel, expression.queryFirst( context));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITextWidgetFeature widgetAdapter = xidget.getFeature( ITextWidgetFeature.class);
      widgetAdapter.setText( channel, Boolean.toString( newValue));
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ITextWidgetFeature widgetAdapter = xidget.getFeature( ITextWidgetFeature.class);
      widgetAdapter.setText( channel, Double.toString( newValue));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      ITextWidgetFeature widgetAdapter = xidget.getFeature( ITextWidgetFeature.class);
      widgetAdapter.setText( channel, newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      ITextWidgetFeature widgetAdapter = xidget.getFeature( ITextWidgetFeature.class);
      widgetAdapter.setText( channel, Xlate.get( object, ""));
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private String channel;
    private IXidget xidget;
  }
}
