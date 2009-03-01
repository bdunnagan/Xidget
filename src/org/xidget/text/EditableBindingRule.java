/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.IBindingRule;
import org.xidget.IXidget;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>enable</i> configuration element.
 */
public class EditableBindingRule implements IBindingRule
{
  /**
   * Create an rule for the specified text channel.
   * @param index The text channel.
   */
  public EditableBindingRule( int index)
  {
    this.index = index;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( xidget, index);
  }
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, int index)
    {
      ITextChannelAdapter channelAdapter = (ITextChannelAdapter)xidget.getAdapter( ITextChannelAdapter.class);
      if ( channelAdapter == null) return;      
      this.channel = channelAdapter.getChannel( index).getWidgetChannel(); 
    }
    
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      channel.setEditable( newValue);
    }

    private IWidgetTextChannel channel;
  }
  
  private int index;
}