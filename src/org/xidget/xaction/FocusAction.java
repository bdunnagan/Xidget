/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IFocusFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that specifies the xidget that has focus (xidget is identified by its configuration).
 */
public class FocusAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    focusExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    if ( focusFeature != null)
    {
      IModelObject config = focusExpr.queryFirst( context);
      IXidget xidget = Creator.getInstance().findXidget( config);
      if ( xidget != null) focusFeature.setFocus( xidget);
    }
    
    return null;
  }

  private IExpression focusExpr;
}
