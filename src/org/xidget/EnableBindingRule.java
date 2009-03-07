/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>enable</i> configuration element.
 */
public class EnableBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( (IWidgetFeature)xidget.getFeature( IWidgetFeature.class));
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( IWidgetFeature adapter)
    {
      this.adapter = adapter;
    }
    
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      adapter.setEnabled( newValue);
    }
    
    private IWidgetFeature adapter;
  }
}
