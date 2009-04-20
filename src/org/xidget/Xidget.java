/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A convenience base implementation of IXidget.
 */
public abstract class Xidget implements IXidget
{
  /**
   * Create the features for this xidget.
   */
  protected abstract void createFeatures();
  
  /**
   * Set the parent xidget.
   * @param parent The parent.
   */
  private final void setParent( IXidget parent)
  {
    if ( this.parent != null) this.parent.getChildren().remove( this);
    this.parent = parent;
    if ( parent != null) parent.getChildren().add( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getParent()
   */
  public final IXidget getParent()
  {
    return parent;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getChildren()
   */
  public final List<IXidget> getChildren()
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
  public final boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    setParent( parent);
    
    // set xidget attribute and save config (bi-directional mapping)
    element.setAttribute( "xidget", this);
    config = element;
    
    // create features
    createFeatures();
    
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#endConfig(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  public final void endConfig( TagProcessor processor, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getConfig()
   */
  public final IModelObject getConfig()
  {
    return config;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#createWidget()
   */
  public final void createWidget()
  {
    IWidgetCreationFeature feature = getFeature( IWidgetCreationFeature.class);
    if ( feature != null) feature.createWidget();
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#destroyWidget()
   */
  public final void destroyWidget()
  {
    IWidgetCreationFeature feature = getFeature( IWidgetCreationFeature.class);
    if ( feature != null) feature.destroyWidget();
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#bind(org.xmodel.xpath.expression.StatefulContext)
   */
  public final void bind( StatefulContext context)
  {
    IBindFeature bindFeature = getFeature( IBindFeature.class);
    if ( bindFeature != null) bindFeature.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public final void unbind( StatefulContext context)
  {
    IBindFeature bindFeature = getFeature( IBindFeature.class);
    if ( bindFeature != null) bindFeature.unbind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatures#getFeature(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    // debug
    Log.printf( "xidget", "Feature '%s' not found on xidget %s.", clss.getSimpleName(), getConfig());
    return null;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    if ( config == null) return "unconfigured";
    return config.getType();
  }

  private IXidget parent;
  private List<IXidget> children;
  private IModelObject config;
}
