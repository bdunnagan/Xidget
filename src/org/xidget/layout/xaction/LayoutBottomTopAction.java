/*
 * Xidget - XML Widgets based on JAHM
 * 
 * LayoutBottomTopAction.java
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
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the bottom side of each xidget to the top side of the next xidget.
 *   - Attach the bottom side of the last xidget to the form.
 */
public class LayoutBottomTopAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xidget.IXidget, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, int spacing)
  {
    ILayoutFeature feature = parent.getFeature( ILayoutFeature.class);
    
    // attach the bottom side of the last xidget to the form
    int last = children.size() - 1;
    feature.attachContainer( children.get( last), Side.bottom, 0);
    
    // attach the bottom side of each xidget to the top side of the next xidget
    for( int i=0; i < last; i++)
    {
      feature.attachPeer( children.get( i), Side.bottom, children.get( i+1), Side.top, -spacing);
    }
  }
}
