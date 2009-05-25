/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IFeatured;
import org.xidget.IXidget;
import org.xidget.ifeature.IContextAssociationFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IContextAssociationFeature.
 */
public class ContextAssociationFeature implements IContextAssociationFeature
{
  public ContextAssociationFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.map = new HashMap<StatefulContext, IFeatured>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IContextAssociationFeature#createFeatures(org.xmodel.xpath.expression.StatefulContext)
   */
  public void createFeatures( StatefulContext context)
  {
    IFeatured features = xidget.createFeatures( context);
    map.put( context, features);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IContextAssociationFeature#deleteFeatures(org.xmodel.xpath.expression.StatefulContext)
   */
  public void deleteFeatures( StatefulContext context)
  {
    map.remove( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IContextAssociationFeature#getFeature(org.xmodel.xpath.expression.StatefulContext, java.lang.Class)
   */
  public <T> T getFeature( StatefulContext context, Class<T> clss)
  {
    IFeatured features = map.get( context);
    if ( features != null) return features.getFeature( clss);
    return null;
  }

  private IXidget xidget;
  private Map<StatefulContext, IFeatured> map;
}
