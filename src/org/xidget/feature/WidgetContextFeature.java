/*
 * Xidget - XML Widgets based on JAHM
 * 
 * WidgetContextFeature.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
