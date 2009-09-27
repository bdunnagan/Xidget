/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A class which manages the details of xpath expression bindings for xidgets.
 */
public class XidgetBinding implements IXidgetBinding
{
  /**
   * Create a binding with the default notification scheme: <i>bind</i>.
   * @param expression The expression.
   * @param listener The listener.
   */
  public XidgetBinding( IExpression expression, IExpressionListener listener)
  {
    this.expression = expression;
    this.listener = listener;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.binding.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void bind( StatefulContext context)
  {
    expression.addNotifyListener( context, listener);
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void unbind( StatefulContext context)
  {
    expression.removeListener( context, listener);
  }
  
  private IExpression expression;
  private IExpressionListener listener;
}
