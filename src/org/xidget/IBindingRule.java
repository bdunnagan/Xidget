/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An interface for an object which returns the listener of a XidgetBinding.
 */
public interface IBindingRule
{
  /**
   * Returns the listener which will be bound to the specified xidget. 
   * The listener must be unique for the xidget.
   * @param xidget The xidget.
   * @return Returns the listener which will be bound to the specified xidget.
   */
  public IExpressionListener getListener( IXidget xidget);
}
