/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Convenient base class for xidgets.
 */
public abstract class AbstractXidget implements IXidget
{
  protected AbstractXidget()
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setParent(org.xidget.IXidget)
   */
  public void setParent( IXidget parent)
  {
    if ( this.parent != null) this.parent.getChildren().remove( this);
    this.parent = parent;
    if ( parent != null) parent.getChildren().add( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getParent()
   */
  public IXidget getParent()
  {
    return parent;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getChildren()
   */
  public List<IXidget> getChildren()
  {
    if ( children == null) children = new ArrayList<IXidget>();
    return children;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setContext(org.xmodel.xpath.expression.StatefulContext)
   */
  public void setContext( StatefulContext context)
  {
    this.context = context;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getContext()
   */
  public StatefulContext getContext()
  {
    return context;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#addBinding(org.xidget.IXidgetBinding)
   */
  public void addBinding( IXidgetBinding binding)
  {
    if ( bindings == null) bindings = new ArrayList<IXidgetBinding>( 3);
    bindings.add( binding);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#bind()
   */
  public void bind()
  {
    if ( bindings == null) return;
    for( IXidgetBinding binding: bindings)
      binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#unbind()
   */
  public void unbind()
  {
    if ( bindings == null) return;
    for( IXidgetBinding binding: bindings)
      binding.unbind( context);
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param element The element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }

  private IXidget parent;
  private List<IXidget> children;
  private StatefulContext context;
  private List<IXidgetBinding> bindings;
}
