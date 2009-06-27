/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that calls the <code>Creator.rebuild</code> method for a specified xidget.
 */
public class RebuildXAction extends GuardedAction
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
      try
      {
        if ( xidget != null) Creator.getInstance().rebuild( xidget);
      }
      catch( TagException e)
      {
        throw new XActionException( "Unable to rebuild xidget: "+xidget, e);
      }
    }
  }
  
  private IExpression xidgetExpr;
}
