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
 *   - Attach the bottom side of each xidget to the top side of the next xidget.
 *   - Attach the bottom side of the last xidget to the form.
 */
public class LayoutBottomTopAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xidget.IXidget, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    // attach the bottom side of the first xidget to the form
    IComputeNode next = getParentNode( parent, Type.bottom);
    IComputeNode node = getComputeNode( children.get( children.size() - 1), Type.bottom);
    node.addDependency( new OffsetNode( next, -margins.y1));
    
    // attach the bottom side of each xidget to the top side of the next xidget
    int last = children.size() - 1;
    for( int i=0; i < last; i++)
    {
      next = getComputeNode( children.get( i+1), Type.top);
      node = getComputeNode( children.get( i), Type.bottom);
      node.addDependency( new OffsetNode( next, -spacing));
    }
  }
}
