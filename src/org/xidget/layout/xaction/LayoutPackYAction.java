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
 *   - Attach the bottom side of the form to the last xidget.
 */
public class LayoutPackYAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    IComputeNode form = getParentNode( parent, Type.bottom);
    IComputeNode node = getComputeNode( children.get( children.size() - 1), Type.bottom);
    form.addDependency( new OffsetNode( node, margins.y1));
  }
}
