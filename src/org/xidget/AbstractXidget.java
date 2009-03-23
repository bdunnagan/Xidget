/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IWidgetFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.layout.ComputeNodeFeature;
import org.xidget.layout.IComputeNodeFeature;
import org.xidget.layout.ILayoutFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.util.Radix;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Convenient base class for xidgets.
 */
public abstract class AbstractXidget implements IXidget
{
  protected AbstractXidget()
  {
  }

  /**
   * Set the parent xidget.
   * @param parent The parent.
   */
  private void setParent( IXidget parent)
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
   * @see org.xidget.IXidget#removeBinding(org.xidget.IXidgetBinding)
   */
  public void removeBinding( IXidgetBinding binding)
  {
    if ( bindings != null) bindings.remove( binding);
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
    setParent( parent);
    
    // set xidget attribute and save config (bi-directional mapping)
    element.setAttribute( "xidget", this);
    config = element;
    
    // load xidget layout
    IModelObject layout = element.getFirstChild( "layout");
    IExpression layoutExpr = Xlate.childGet( element, "layout", Xlate.get( element, "layout", (IExpression)null));
    
    // if declared in-place
    if ( layout != null && layoutExpr == null)
      setLayout( processor, layout);

    // reference to layout
    if ( layoutExpr != null)
    {
      StatefulContext context = new StatefulContext( processor.getContext(), (layout != null)? layout: element);
      IModelObject declaration = layoutExpr.queryFirst( context);
      if ( declaration == null) throw new TagException( "Declaration not found for layout: "+element);
      setLayout( processor, declaration);      
    }
        
    // build widget hierarchy 
    String label = Xlate.childGet( element, "label", (String)null);
    IWidgetCreationFeature creationFeature = getFeature( IWidgetCreationFeature.class);
    if ( creationFeature == null)
      throw new TagException( 
        "IWidgetCreationFeature is required for all xidget implementations.");
    
    creationFeature.createWidget( this, label, element);
    
    return true;
  }

  /**
   * Stubbed implementation for convenience.
   * @param processor The tag processor.
   * @param element The element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
    // debug compute node
    IWidgetFeature feature = getFeature( IWidgetFeature.class);
    if ( feature != null) System.out.printf( "@%s IS %s\n", Radix.convert( feature.hashCode(), 36), element);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getConfig()
   */
  public IModelObject getConfig()
  {
    return config;
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatures#setFeature(java.lang.Object)
   */
  public void setFeature( Class<? extends Object> featureClass, Object feature)
  {
    throw new StaticFeatureException( feature);
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatures#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IComputeNodeFeature.class)
    {
      if ( computeNodeFeature == null) computeNodeFeature = new ComputeNodeFeature( this);
      return (T)computeNodeFeature;
    }
    return null;
  }

  /**
   * Set the layout on the xidget associated with the specified tag handler.
   * @param processor The processor.
   * @param declaration The layout declaration.
   */
  private void setLayout( TagProcessor processor, IModelObject declaration)
  {
    ILayoutFeature feature = getFeature( ILayoutFeature.class);
    if ( feature == null) feature = getParent().getFeature( ILayoutFeature.class);
    if ( feature != null) feature.configure( processor, declaration);      
  }

  private IModelObject config;
  private IXidget parent;
  private List<IXidget> children;
  private StatefulContext context;
  private List<IXidgetBinding> bindings;
  private IComputeNodeFeature computeNodeFeature;
}
