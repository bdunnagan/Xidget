/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.binding.IXidgetBinding;
import org.xidget.ifeature.IBindFeature;
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
  
  /**
   * Create a bind feature and specify children that should not be bound.
   * @param xidget The xidget to which the feature belongs.
   * @param ignore The element names of the children to be ignored.
   */
  public BindFeature( IXidget xidget, String[] ignore)
  {
    this( xidget);
    this.ignore = Arrays.asList( ignore);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IBindFeature#addBindingBeforeChildren(org.xidget.binding.IXidgetBinding)
   */
  public void addBindingBeforeChildren( IXidgetBinding binding)
  {
    if ( bindBeforeChildren == null) bindBeforeChildren = new ArrayList<IXidgetBinding>();
    bindBeforeChildren.add( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IBindFeature#addBindingAfterChildren(org.xidget.binding.IXidgetBinding)
   */
  public void addBindingAfterChildren( IXidgetBinding binding)
  {
    if ( bindAfterChildren == null) bindAfterChildren = new ArrayList<IXidgetBinding>();
    bindAfterChildren.add( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#remove(org.xidget.IXidgetBinding)
   */
  public void remove( IXidgetBinding binding)
  {
    if ( bindAfterChildren != null) bindAfterChildren.remove( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#bind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void bind( StatefulContext context)
  {
    Log.printf( "xidget", "bind: %s with %s\n", xidget, context);
    
    // internal bindings
    if ( bindBeforeChildren != null)
      for( IXidgetBinding binding: bindBeforeChildren)
        binding.bind( context);
    
    // bind children
    for( IXidget child: xidget.getChildren())
    {
      if ( ignore == null || !ignore.contains( child.getConfig().getType()))
      {
        IBindFeature bindFeature = child.getFeature( IBindFeature.class);
        bindFeature.bind( context);
      }
    }
    
    // internal bindings
    if ( bindAfterChildren != null)
      for( IXidgetBinding binding: bindAfterChildren)
        binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.IBindFeature#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void unbind( StatefulContext context)
  {
    Log.printf( "xidget", "unbind: %s with %s\n", xidget, context);
    
    // internal bindings
    if ( bindBeforeChildren != null)
      for( IXidgetBinding binding: bindBeforeChildren)
        binding.unbind( context);
    
    // unbind children
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      bindFeature.unbind( context);
    }

    // internal bindings
    if ( bindAfterChildren != null)
      for( IXidgetBinding binding: bindAfterChildren)
        binding.unbind( context);
  }
  
  protected IXidget xidget;
  protected List<IXidgetBinding> bindBeforeChildren;
  protected List<IXidgetBinding> bindAfterChildren;
  private List<String> ignore;
}
