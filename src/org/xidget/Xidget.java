/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * A universal implementation of IXidget. Since xidgets are configured by specifying 
 * their features, it is rarely necessary to have customized subclasses.  Refer to 
 * the feature sets for xidgets for information about how to configure each type of
 * xidget.
 */
public final class Xidget implements IXidget
{
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

  /* (non-Javadoc)
   * @see org.xidget.IFeatures#getFeature(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    // debug
    if ( IXidget.debug) System.err.printf( "Feature %s not found on xidget %s.", clss.getSimpleName(), getConfig());
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
