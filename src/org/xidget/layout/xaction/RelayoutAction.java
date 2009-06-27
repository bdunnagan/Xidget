/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that calls the <code>IWidgetContainerFeature.relayout</code> method.
 */
public class RelayoutAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    xidgetExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    for( IModelObject element: xidgetExpr.query( context, null))
    {
      IXidget xidget = (IXidget)element.getAttribute( "xidget");
      if ( xidget != null)
      {
        IWidgetContainerFeature feature = xidget.getFeature( IWidgetContainerFeature.class);
        if ( feature != null) feature.relayout();
      }
    }
  }
  
  private IExpression xidgetExpr;
}
