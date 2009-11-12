/*
 * Xidget - XML Widgets based on JAHM
 * 
 * LayoutGridYAction.java
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
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the bottom side of each xidget to the grid, offset by half the interstice.
 *   - Attach the top side of each xidget to the previous xidget, offset by the interstice.
 *   - Attach the top side of the first xidget to the form.
 *   - Attach the bottom side of the last xidget to the form.
 *   - Specify handles by annotating each xidget.
 */
public class LayoutGridYAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    handleExpr = document.getExpression( "handle", true);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, int spacing)
  {
    ILayoutFeature feature = parent.getFeature( ILayoutFeature.class);
    
    int halfSpacing = spacing / 2;
    float dy = 1f / children.size();
    float y = dy;
    int last = children.size() - 1;
    boolean handle = (handleExpr != null)? handleExpr.evaluateBoolean( context): false;
    
    // attach the bottom side of each xidget to the grid, offset by half the interstice
    for( int i=0; i < last; i++, y += dy)
    {
      IXidget child = children.get( i);
      feature.attachContainer( child, Side.bottom, y, -halfSpacing, handle);
    }
    
    // attach the top side of each xidget to the previous xidget, offset by the interstice
    for( int i=1; i <= last; i++)
    {
      feature.attachPeer( children.get( i), Side.top, children.get( i-1), Side.bottom, spacing);
    }
    
    // attach the top side of the first xidget to the form
    feature.attachContainer( children.get( 0), Side.top, 0);
    
    // attach the bottom side of the last xidget to the form
    feature.attachContainer( children.get( last), Side.bottom, 0);
  }
  
  private IExpression handleExpr;
}
