/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xidget.layout.Margins;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the top side of each xidget to the bottom side of the previous xidget.
 *   - Attach the top side of the first xidget to the form.
 */
public class LayoutTopBottomAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    ILayoutFeature feature = parent.getFeature( ILayoutFeature.class);
    
    // attach the top side of the first xidget to the form
    feature.attachContainer( children.get( 0), Side.top, margins.y0);
    
    // attach the top side of each xidget to the bottom side of the previous xidget
    for( int i=1; i<children.size(); i++)
    {
      feature.attachPeer( children.get( i), Side.top, children.get( i-1), Side.bottom, spacing);
    }
  }
}
