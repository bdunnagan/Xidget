/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IXidgetBinding;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IBindFeature which does not bind the xidgets children. This is useful
 * if another feature needs to control when and how the children are bound.
 */
public class BindFeature implements IBindFeature
{
  public BindFeature( IXidget xidget)
  {
    this.xidget = xidget;
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
    // debug
    if ( IXidget.debug) System.out.printf( "bind: %s with %s\n", xidget, context);
    
    // bind children first
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      bindFeature.bind( context);
    }
    
    // internal bindings
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void unbind( StatefulContext context)
  {
    // debug
    if ( IXidget.debug) System.out.printf( "unbind: %s with %s\n", xidget, context);
    
    // unbind children first
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      bindFeature.unbind( context);
    }

    // internal bindings
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.unbind( context);
  }
  
  private IXidget xidget;
  private List<IXidgetBinding> bindings;
}
