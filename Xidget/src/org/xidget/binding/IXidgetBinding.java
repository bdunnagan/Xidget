/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for things that are bound when a xidget is bound. Each instance is owned
 * by a single xidget and its bind and unbind methods are called when the xidget bind
 * and unbind methods are called.
 */
public interface IXidgetBinding
{
  public enum Notify { bind, unbind, both, none};
  
  /**
   * Bind the listener to the expression in the specified context.
   * @param context The context.
   * @param notify True if initial notification should be performed.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind the listener from the expression in the specified context.
   * @param context The context.
   */
  public void unbind( StatefulContext context);
}
