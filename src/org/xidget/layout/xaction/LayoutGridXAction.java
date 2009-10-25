/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xidget.layout.Margins;
import org.xmodel.xaction.XActionDocument;
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
    handleExpr = document.getExpression( "handle", true);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.xaction.AbstractLayoutAction#layout(org.xmodel.xpath.expression.IContext, org.xidget.IXidget, java.util.List, org.xidget.layout.Margins, int)
   */
  @Override
  protected void layout( IContext context, IXidget parent, List<IXidget> children, Margins margins, int spacing)
  {
    ILayoutFeature feature = parent.getFeature( ILayoutFeature.class);
    
    int halfSpacing = spacing / 2;
    float dx = 1f / children.size();
    float x = dx;
    int last = children.size() - 1;
    boolean handle = (handleExpr != null)? handleExpr.evaluateBoolean( context): false;
    
    // attach the right side of each xidget to the grid, offset by half the interstice
    for( int i=0; i < last; i++, x += dx)
    {
      IXidget child = children.get( i);
      feature.attachContainer( child, Side.right, x, -halfSpacing, handle);
    }
    
    // attach the left side of each xidget to the previous xidget, offset by the interstice
    for( int i=1; i <= last; i++)
    {
      feature.attachPeer( children.get( i), Side.left, children.get( i-1), Side.right, spacing);
    }
    
    // attach the left side of the first xidget to the form
    feature.attachContainer( children.get( 0), Side.left, margins.x0);
    
    // attach the right side of the last xidget to the form
    feature.attachContainer( children.get( last), Side.right, -margins.x1);
  }
  
  private IExpression handleExpr;
}