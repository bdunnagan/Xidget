/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.OffsetNode;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the bottom side of each xidget to the grid, offset by half the interstice.
 *   - Attach the top side of each xidget to the previous xidget, offset by the interstice.
 *   - Attach the top side of the first xidget to the form.
 *   - Attach the bottom side of the last xidget to the form.
 *   - Specify handles by annotating each xidget.
 */
public class LayoutGridYAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    handleExpr = document.getExpression( "handle", true);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    int halfSpacing = spacing / 2;
    float y = 0;
    float dy = 1f / children.size();
    int last = children.size() - 1;
    boolean handle = (handleExpr != null)? handleExpr.evaluateBoolean( context): false;
    
    // attach the bottom side of each xidget to the grid, offset by half the interstice
    for( int i=0; i < last; i++)
    {
      IXidget child = children.get( i);
      y += dy;
      IComputeNode node = getComputeNode( child, Type.bottom);
      AnchorNode anchor = new AnchorNode( parent, Type.bottom, y, -halfSpacing);
      if ( handle) anchor.setHandle( true);
      node.addDependency( anchor);
    }
    
    // attach the top side of each xidget to the previous xidget, offset by the interstice
    for( int i=1; i <= last; i++)
    {
      IXidget previous = children.get( i-1);
      IXidget child = children.get( i);
      IComputeNode node1 = getComputeNode( child, Type.top);
      IComputeNode node2 = getComputeNode( previous, Type.bottom);
      node1.addDependency( new OffsetNode( node2, spacing));
    }
    
    // attach the top side of the first xidget to the form
    IComputeNode form = getParentNode( parent, Type.top);
    IComputeNode node = getComputeNode( children.get( 0), Type.top);
    node.addDependency( new OffsetNode( form, margins.y0));
    
    // attach the bottom side of the last xidget to the form
    form = getParentNode( parent, Type.bottom);
    node = getComputeNode( children.get( last), Type.bottom);
    node.addDependency( new OffsetNode( form, -margins.y1));
  }
  
  private IExpression handleExpr;
}
