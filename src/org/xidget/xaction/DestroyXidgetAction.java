/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that calls the <code>Creator.destroy</code> method for a specified xidget.
 */
public class DestroyXidgetAction extends GuardedAction
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
  protected Object[] doAction( IContext context)
  {
    for( IModelObject element: xidgetExpr.query( context, null))
    {
      IXidget xidget = (IXidget)element.getAttribute( "instance");
      if ( xidget != null) 
      {
        Creator.getInstance().destroy( xidget);
      }
      else
      {
        xidget = (IXidget)element.getValue();
        if ( xidget != null) Creator.getInstance().destroy( xidget);
      }
    }
    
    return null;
  }
  
  private IExpression xidgetExpr;
}
