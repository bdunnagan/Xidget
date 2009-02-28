/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import java.util.List;
import org.xidget.IBindingRule;
import org.xidget.IXidget;
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
  /**
   * Create a rule for the specified text channel.
   * @param channel The text channel.
   */
  public TextBindingRule( String channel)
  {
    this.channel = channel;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( xidget, channel);
  }

  final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      ITextChannelAdapter channelAdapter = (ITextChannelAdapter)xidget.getAdapter( ITextChannelAdapter.class);
      if ( channelAdapter == null) return;      
      this.channel = channelAdapter.getChannel( channel); 
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( nodes.contains( channel.getSource())) return;
      channel.setSource( nodes.get( 0));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( !nodes.contains( channel.getSource())) return;
      channel.setSource( expression.queryFirst( context));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      channel.setTextFromModel( Boolean.toString( newValue));
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      channel.setTextFromModel( Double.toString( newValue));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      channel.setTextFromModel( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      channel.setTextFromModel( Xlate.get( object, ""));
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private TextChannel channel;
  }
  
  private String channel;
}
