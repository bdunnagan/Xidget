/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction that closes a dialog.
 */
public class CloseDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    IModelObject holder = xidgetExpr.queryFirst( context);
    if ( holder == null) return;
    
    IXidget xidget = (IXidget)holder.getValue();
    IDialogFeature feature = xidget.getFeature( IDialogFeature.class);
    feature.close( (StatefulContext)context);
  }
  
  private final IExpression xidgetExpr = XPath.createExpression( "$dialog"); 
}
