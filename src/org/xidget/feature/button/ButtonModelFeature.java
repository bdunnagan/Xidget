/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ButtonModelFeature.java
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
package org.xidget.feature.button;

import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.button.IButtonModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of button model feature.
 */
public class ButtonModelFeature implements IButtonModelFeature
{
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( StatefulContext context, String channel, IModelObject node)
  {
    if ( channel == TextModelFeature.allChannel) this.node = node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    return (channel == TextModelFeature.allChannel)? node: null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.button.IButtonModelFeature#setState(boolean)
   */
  public void setState( boolean state)
  {
    if ( node != null) Xlate.set( node, state);
  }
  
  private IModelObject node;
}
