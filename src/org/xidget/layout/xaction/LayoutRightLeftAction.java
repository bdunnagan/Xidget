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
 *   - Attach the right side of each xidget to the left side of the next xidget.
 *   - Attach the right side of the last xidget to the form.
 */
public class LayoutRightLeftAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    // attach the right side of the first xidget to the form
    IComputeNode next = getParentNode( parent, Type.right);
    IComputeNode node = getComputeNode( children.get( children.size() - 1), Type.right);
    node.addDependency( new OffsetNode( next, -margins.x1));
    
    // attach the right side of each xidget to the left side of the next xidget
    int last = children.size() - 1;
    for( int i=0; i < last; i++)
    {
      next = getComputeNode( children.get( i+1), Type.left);
      node = getComputeNode( children.get( i), Type.right);
      node.addDependency( new OffsetNode( next, -spacing));
    }
  }
}
