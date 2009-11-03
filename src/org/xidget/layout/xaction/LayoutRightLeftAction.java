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
 *   - Attach the right side of each xidget to the left side of the next xidget.
 *   - Attach the right side of the last xidget to the form.
 */
public class LayoutRightLeftAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, int spacing)
  {
    ILayoutFeature feature = parent.getFeature( ILayoutFeature.class);
    
    // attach the right side of the last xidget to the form
    int last = children.size() - 1;
    feature.attachContainer( children.get( last), Side.right, 0);
    
    
    // attach the right side of each xidget to the left side of the next xidget
    for( int i=0; i < last; i++)
    {
      feature.attachPeer( children.get( i), Side.right, children.get( i+1), Side.left, -spacing);
    }
  }
}
