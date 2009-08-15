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
import org.xidget.layout.MaxNode;
import org.xidget.layout.OffsetNode;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the bottom side of the form to the last xidget.
 */
public class LayoutMaxYAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    MaxNode max = new MaxNode();
    for( int i=children.size() - 1; i >= 0; i--)
    {
      IComputeNode node = getComputeNode( children.get( i), Type.bottom);
      max.addDependency( node);
    }
    
    IComputeNode form = getParentNode( parent, Type.bottom);
    form.addDependency( new OffsetNode( max, margins.y1));
  }
}
