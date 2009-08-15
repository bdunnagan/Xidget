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
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that creates the following layout attachments:
 *   - Attach the right side of each xidget to the grid, offset by half the interstice.
 *   - Attach the left side of each xidget to the previous xidget, offset by the interstice.
 *   - Attach the left side of the first xidget to the form.
 *   - Attach the right side of the last xidget to the form.
 *   - Specify handles by annotating each xidget.
 */
public class LayoutGridXAction extends AbstractLayoutAction
{
  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    handlesExpr = XPath.createExpression( "../@handles = 'true'");
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    int halfSpacing = spacing / 2;
    float x = 0;
    float dx = 1f / children.size();
    int last = children.size() - 1;
    boolean handles = handlesExpr.evaluateBoolean( context);
    
    // attach the right side of each xidget to the grid, offset by half the interstice
    for( int i=1; i < last; i++)
    {
      IXidget child = children.get( i);
      x += dx;
      IComputeNode node = getComputeNode( child, Type.right);
      AnchorNode anchor = new AnchorNode( parent, Type.right, x, -halfSpacing);
      if ( handles) anchor.setHandle( true);
      node.addDependency( anchor);
    }
    
    // attach the left side of each xidget to the previous xidget, offset by the interstice
    for( int i=1; i <= last; i++)
    {
      IXidget previous = children.get( i-1);
      IXidget child = children.get( i);
      IComputeNode node1 = getComputeNode( child, Type.left);
      IComputeNode node2 = getComputeNode( previous, Type.right);
      node1.addDependency( new OffsetNode( node2, spacing));
    }
    
    // attach the left side of the first xidget to the form
    IComputeNode form = getParentNode( parent, Type.left);
    IComputeNode node = getComputeNode( children.get( 0), Type.left);
    node.addDependency( new OffsetNode( form, margins.x0));
    
    // attach the right side of the last xidget to the form
    form = getParentNode( parent, Type.right);
    node = getComputeNode( children.get( last), Type.right);
    node.addDependency( new OffsetNode( form, -margins.x1));
  }
  
  private IExpression handlesExpr;
}