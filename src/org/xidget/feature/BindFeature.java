/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.binding.IXidgetBinding;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;
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
    this.contexts = new ArrayList<StatefulContext>( 1);
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
   * @see org.xidget.ifeature.IBindFeature#bind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void bind( StatefulContext context, boolean notify)
  {
    Log.printf( "xidget", "bind: %s with %s\n", xidget, context);
    
    // add context to list
    contexts.add( context);
    
    // optionally assign xidget to variable
    String variable = Xlate.get( xidget.getConfig(), "assign", (String)null);
    if ( variable != null)
    {
      IModelObject holder = new ModelObject( "xidget");
      holder.setValue( xidget);
      context.set( variable, holder);
    }
    
    // create widget-context association
    IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
    IWidgetCreationFeature widgetCreationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( widgetContextFeature != null && widgetCreationFeature != null) 
    {
      Object[] widgets = widgetCreationFeature.getLastWidgets();
      if ( widgets != null)
      {
        for( Object widget: widgets)
          widgetContextFeature.createAssociation( widget, context);
      }
    }
    
    // call onOpen script
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    if ( scriptFeature != null) scriptFeature.runScript( "onOpen", context);
    
    // internal bindings
    if ( bindBeforeChildren != null)
      for( IXidgetBinding binding: bindBeforeChildren)
        binding.bind( context, notify);
    
    // bind children
    for( IXidget child: xidget.getChildren())
    {
      if ( ignore == null || !ignore.contains( child.getConfig().getType()))
      {
        IBindFeature bindFeature = child.getFeature( IBindFeature.class);
        bindFeature.bind( context, notify);
      }
    }
    
    // internal bindings
    if ( bindAfterChildren != null)
      for( IXidgetBinding binding: bindAfterChildren)
        binding.bind( context, notify);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IBindFeature#unbind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void unbind( StatefulContext context, boolean notify)
  {
    Log.printf( "xidget", "unbind: %s with %s\n", xidget, context);
    
    if ( !contexts.remove( context)) return;
    
    // call onClose script
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    if ( scriptFeature != null) scriptFeature.runScript( "onClose", context);
    
    // internal bindings
    if ( bindBeforeChildren != null)
      for( IXidgetBinding binding: bindBeforeChildren)
        binding.unbind( context, notify);
    
    // unbind children
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      bindFeature.unbind( context, notify);
    }

    // internal bindings
    if ( bindAfterChildren != null)
      for( IXidgetBinding binding: bindAfterChildren)
        binding.unbind( context, notify);
    
    // remove widget-context association
    IWidgetContextFeature widgetContextFeature = xidget.getFeature( IWidgetContextFeature.class);
    IWidgetCreationFeature widgetCreationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    if ( widgetContextFeature != null && widgetCreationFeature != null) 
    {
      Object[] widgets = widgetCreationFeature.getLastWidgets();
      if ( widgets != null)
      {
        for( Object widget: widgets)
          widgetContextFeature.removeAssociation( widget);
      }
    }
    
    // unassign variable
    String variable = Xlate.get( xidget.getConfig(), "assign", (String)null);
    if ( variable != null) context.set( variable, Collections.<IModelObject>emptyList());
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IBindFeature#getBoundContexts()
   */
  public List<StatefulContext> getBoundContexts()
  {
    return contexts;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IBindFeature#getBoundContext()
   */
  public StatefulContext getBoundContext()
  {
    if ( contexts.size() > 1) throw new IllegalStateException( "More than one context is bound to: "+xidget);
    return (contexts.size() > 0)? contexts.get( 0): null;
  }

  protected IXidget xidget;
  protected List<StatefulContext> contexts;
  protected List<IXidgetBinding> bindBeforeChildren;
  protected List<IXidgetBinding> bindAfterChildren;
  private List<String> ignore;
}
