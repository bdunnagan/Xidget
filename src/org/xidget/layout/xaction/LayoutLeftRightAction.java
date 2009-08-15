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
 *   - Attach the left side of each xidget to the right side of the previous xidget.
 *   - Attach the left side of the first xidget to the form.
 */
public class LayoutLeftRightAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    // attach the left side of the first xidget to the form
    IComputeNode previous = getParentNode( parent, Type.left);
    IComputeNode node = getComputeNode( children.get( 0), Type.left);
    node.addDependency( new OffsetNode( previous, margins.x0));
    
    // attach the left side of each xidget to the right side of the previous xidget
    for( int i=1; i<children.size(); i++)
    {
      previous = getComputeNode( children.get( i-1), Type.right);
      node = getComputeNode( children.get( i), Type.left);
      node.addDependency( new OffsetNode( previous, spacing));
    }
  }
}
