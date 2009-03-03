/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.layout.LayoutTagHandler.Layout;
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
    // children
    if ( children != null)
      for( IXidget child: children)
      {
        child.setContext( getContext());
        child.bind();
      }
    
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#unbind()
   */
  public void unbind()
  {
    // internal bindings
    if ( bindings != null)
      for( IXidgetBinding binding: bindings)
        binding.unbind( context);

    // children
    if ( children != null)
      for( IXidget child: children)
        child.unbind();
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param parent Null or the parent of this xidget.
   * @param element The configuration element.
   * @return Returns true.
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    return true;
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param element The element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }
 
  /* (non-Javadoc)
   * @see org.xidget.IXidget#getLayout()
   */
  public Layout getLayout()
  {
    return layout;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setLayout(org.xidget.layout.LayoutTagHandler.Layout)
   */
  public void setLayout( Layout layout)
  {
    this.layout = layout;
  }

  private IXidget parent;
  private List<IXidget> children;
  private StatefulContext context;
  private List<IXidgetBinding> bindings;
  private Layout layout;
}
