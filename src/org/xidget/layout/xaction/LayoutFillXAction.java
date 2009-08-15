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
 *   - Attach the left and right side of each xidget to the form.
 */
public class LayoutFillXAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    IComputeNode parentLeft = getParentNode( parent, Type.left);
    IComputeNode parentRight = getParentNode( parent, Type.right);
    
    for( IXidget child: parent.getChildren())
    {
      IComputeNode left = getComputeNode( child, Type.left);
      left.addDependency( new OffsetNode( parentLeft, margins.x0));
      
      IComputeNode right = getComputeNode( child, Type.right);
      right.addDependency( new OffsetNode( parentRight, -margins.x1));
    }
  }
}
