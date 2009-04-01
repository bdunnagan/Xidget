/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An interface for an object which returns the listener of a XidgetBinding.
 */
public interface IBindingRule
{
  /**
   * Returns true if this rule applies to the specified xidget and configuration element.
   * @param xidget The xidget parent.
   * @param element The rule configuration element.
   * @return Returns true if this rule applies.
   */
  public boolean applies( IXidget xidget, IModelObject element);
  
  /**
   * Returns the listener which will be bound to the specified xidget. 
   * The listener must be unique for the xidget.
   * @param xidget The xidget.
   * @param element The configuration element that created the binding.
   * @return Returns the listener which will be bound to the specified xidget.
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element);
}
