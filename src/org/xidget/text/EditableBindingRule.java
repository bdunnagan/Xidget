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
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget)
  {
    return new Listener( (ITextWidgetAdapter)xidget.getAdapter( ITextWidgetAdapter.class));
  }  
}

final class Listener extends ExpressionListener
{
  Listener( ITextWidgetAdapter adapter)
  {
    this.adapter = adapter;
  }
  
  public void notifyChange( IExpression expression, IContext context, boolean newValue)
  {
    adapter.setEditable( newValue);
  }
  
  private ITextWidgetAdapter adapter;
}
