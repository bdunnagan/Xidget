/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.binding.IXidgetBinding;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the binding feature of a xidget.
 */
public interface IBindFeature
{
  /**
   * Add a binding.
   * @param binding The binding.
   */
  public void add( IXidgetBinding binding);
  
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
