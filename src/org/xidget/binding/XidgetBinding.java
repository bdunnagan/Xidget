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
    this( expression, listener, Notify.bind);
  }
  
  /**
   * Create a binding and specify the notification scheme.
   * @param expression The expression.
   * @param listener The listener.
   * @param notify The notification scheme.
   */
  public XidgetBinding( IExpression expression, IExpressionListener listener, Notify notify)
  {
    this.expression = expression;
    this.listener = listener;
    this.notify = notify;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void bind( StatefulContext context)
  {
    if ( notify == Notify.bind || notify == Notify.both) 
      expression.addNotifyListener( context, listener); 
    else expression.addListener( context, listener);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void unbind( StatefulContext context)
  {
    if ( notify == Notify.unbind || notify == Notify.both) 
      expression.removeNotifyListener( context, listener); 
    else expression.removeListener( context, listener);
  }
  
  private Notify notify;
  private IExpression expression;
  private IExpressionListener listener;
}
