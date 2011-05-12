/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.IXidget;
import org.xidget.ifeature.ISourceFeature;
import org.xidget.ifeature.IValueFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of ISourceFeature that supports validation and transformation. This implementation
 * expects the xidget to export IValueFeature for setting the value displayed by a xidget.
 */
public class SourceFeature implements ISourceFeature 
{
  public SourceFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(org.xmodel.IModelObject)
   */
  @Override
  public void setSource( IModelObject node)
  {
    boolean changed = this.node != node;
    
    this.node = node;
    
    if ( changed)
    {
      IValueFeature feature = xidget.getFeature( IValueFeature.class);
      if ( feature != null) feature.display( node.getValue());
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource()
   */
  @Override
  public IModelObject getSource()
  {
    return node;
  }
  
  private IXidget xidget;
  private IModelObject node;
}
