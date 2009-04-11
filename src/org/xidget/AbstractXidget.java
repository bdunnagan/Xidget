/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.BindFeature;
import org.xidget.feature.IBindFeature;
import org.xidget.feature.IWidgetCreationFeature;
import org.xidget.layout.ComputeNodeFeature;
import org.xidget.layout.IComputeNodeFeature;
import org.xidget.layout.ILayoutFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
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
    
    // create these features early
    bindFeature = new BindFeature( this);
    computeNodeFeature = new ComputeNodeFeature( this);
    
    // configure layout
    configureLayout( processor, element);
    
    // create widget hierarchy
    widgetCreationFeature = getWidgetCreationFeature();
    if ( widgetCreationFeature != null)
    {
      String label = Xlate.childGet( element, "label", (String)null);
      widgetCreationFeature.createWidget( label, element);
    }
    
    // create features
    createFeatures();
    
    // configure features
    configureFeatures();
    
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getConfig()
   */
  public IModelObject getConfig()
  {
    return config;
  }

  /**
   * Configure the ILayoutFeature.
   * @param processor The tag processor.
   * @param element The configuration element.
   */
  private void configureLayout( TagProcessor processor, IModelObject element) throws TagException
  {
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
  }
  
  /**
   * Returns null or the required IWidgetCreationFeature.
   * @return Returns null or the required IWidgetCreationFeature.
   */
  protected abstract IWidgetCreationFeature getWidgetCreationFeature();

  /**
   * Create the ILayoutFeature for this xidget if there is one. 
   * This method is called before the widget hierarchy is created. 
   */
  protected void createLayoutFeature()
  {
  }
  
  /**
   * Create the features for this xidget. This method is called after the widget
   * hierarchy has been created, so the widgets are guaranteed to exist. This means
   * that features that depend on widgets can directly reference the widget.
   */
  protected void createFeatures()
  {
  }
  
  /**
   * Called after the widget hierarchy has been created and the <code>createFeatures</code>
   * method has been called. This method should perform whatever configuration of the
   * features specific to this xidget that is required.
   */
  protected void configureFeatures()
  {
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IFeatures#setFeature(java.lang.Object)
   */
  public void setFeature( Class<? extends Object> featureClass, Object feature)
  {
    if ( featureClass == IBindFeature.class) bindFeature = (IBindFeature)feature;
    else throw new StaticFeatureException( feature);
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatures#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == IComputeNodeFeature.class) return (T)computeNodeFeature;
    if ( clss == IWidgetCreationFeature.class) return (T)widgetCreationFeature;
    
    // debug
    if ( IXidget.debug) System.err.printf( "Feature %s not found on xidget %s.", clss.getSimpleName(), getConfig());
    
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
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    if ( config == null) return "unconfigured";
    return config.getType();
  }

  private IModelObject config;
  private IXidget parent;
  private List<IXidget> children;
  private IBindFeature bindFeature;
  private IComputeNodeFeature computeNodeFeature;
  private IWidgetCreationFeature widgetCreationFeature;
}
