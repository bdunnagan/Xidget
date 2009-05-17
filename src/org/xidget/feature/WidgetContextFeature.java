/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;
import org.xidget.Log;
import org.xidget.ifeature.IWidgetContextFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IWidgetContextFeature.
 */
public class WidgetContextFeature implements IWidgetContextFeature
{
  public WidgetContextFeature()
  {
    map = new HashMap<Object, StatefulContext>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContextFeature#createAssociation(java.lang.Object, org.xmodel.xpath.expression.StatefulContext)
   */
  public void createAssociation( Object widget, StatefulContext context)
  {
    map.put( widget, context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContextFeature#removeAssociation(java.lang.Object)
   */
  public void removeAssociation( Object widget)
  {
    map.remove( widget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IWidgetContextFeature#getContext(java.lang.Object)
   */
  public StatefulContext getContext( Object widget)
  {
    StatefulContext context = map.get( widget);
    if ( context == null) Log.printf( "xidget", "No context associated with widget: %s\n", widget);
    return context;
  }

  private Map<Object, StatefulContext> map;
}
