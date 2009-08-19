/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.OffsetNode;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the top and bottom side of each xidget to the form.
 */
public class LayoutFillYAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    IComputeNode parentTop = getParentNode( parent, Type.top);
    IComputeNode parentBottom = getParentNode( parent, Type.bottom);
    
    for( IXidget child: children)
    {
      IComputeNode top = getComputeNode( child, Type.top);
      top.addDependency( new OffsetNode( parentTop, margins.y0));
      
      IComputeNode bottom = getComputeNode( child, Type.bottom);
      bottom.addDependency( new OffsetNode( parentBottom, -margins.y1));
    }
  }
}
