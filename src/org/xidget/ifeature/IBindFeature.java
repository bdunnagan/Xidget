/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xidget.binding.IXidgetBinding;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the binding feature of a xidget.
 */
public interface IBindFeature
{
  /**
   * Add a binding which will be performed before children are bound.
   * @param binding The binding.
   */
  public void addBindingBeforeChildren( IXidgetBinding binding);
  
  /**
   * Add a binding which will be performed after children are bound.
   * @param binding The binding.
   */
  public void addBindingAfterChildren( IXidgetBinding binding);
  
  /**
   * Remove a binding.
   * @param binding The binding.
   */
  public void remove( IXidgetBinding binding);
  
  /**
   * Bind to the specified context.
   * @param context The context.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind from the specified context.
   * @param context The context.
   */
  public void unbind( StatefulContext context);
}
