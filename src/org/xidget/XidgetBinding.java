/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A class which manages the details of xpath expression bindings for xidgets.
 */
public class XidgetBinding
{
  public enum Notify { bind, unbind, both, none};
  
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
  
  /**
   * Bind the listener to the expression in the specified context.
   * @param context The context.
   * @param notify True if initial notification should be performed.
   */
  public void bind( StatefulContext context)
  {
    if ( notify == Notify.bind || notify == Notify.both) 
      expression.addNotifyListener( context, listener); 
    else expression.addListener( context, listener);
  }
  
  /**
   * Unbind the listener from the expression in the specified context.
   * @param context The context.
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
