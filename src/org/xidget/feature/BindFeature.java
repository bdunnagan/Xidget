/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.binding.IXidgetBinding;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of the IBindFeature.
 */
public class BindFeature implements IBindFeature
{
  public BindFeature()
  {
    contexts = new ArrayList<StatefulContext>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#add(org.xidget.IXidgetBinding)
   */
  public void add( IXidgetBinding binding)
  {
    if ( bindings == null) bindings = new ArrayList<IXidgetBinding>();
    bindings.add( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#remove(org.xidget.IXidgetBinding)
   */
  public void remove( IXidgetBinding binding)
  {
    if ( bindings != null) bindings.remove( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#bind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void bind( StatefulContext context)
  {
    if ( bindings == null) return;
    
    contexts.add( context);
    
    for( IXidgetBinding binding: bindings)
      binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void unbind( StatefulContext context)
  {
    if ( bindings == null) return;
    
    try
    {
      for( IXidgetBinding binding: bindings)
        binding.unbind( context);
    }
    finally
    {
      contexts.remove( context);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#getBoundContexts()
   */
  public List<StatefulContext> getBoundContexts()
  {
    return contexts;
  }

  private List<IXidgetBinding> bindings;
  private List<StatefulContext> contexts;
}
