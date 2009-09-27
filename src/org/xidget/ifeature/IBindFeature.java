/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
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

  /**
   * Returns the list of currently bound contexts.
   * @return Returns the list of currently bound contexts.
   */
  public List<StatefulContext> getBoundContexts();
  
  /**
   * Returns the singular context that was bound. If more than one context has been
   * bound then this method throws an exception. Most xidgets are only ever bound
   * to one context. These xidgets have features that are dependent on this property.
   * @return Returns null or the singular context that was bound.
   */
  public StatefulContext getBoundContext();
}
