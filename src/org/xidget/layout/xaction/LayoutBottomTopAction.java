/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
