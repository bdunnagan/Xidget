/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that sets the layout margins.
 */
public class SpacingAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure(document);
    spacingExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    double spacing = spacingExpr.evaluateNumber( context);
    
    IModelObject object = context.getObject();
    IXidget xidget = (IXidget)object.getAttribute( "instance");
    if ( xidget != null)
    {
      ILayoutFeature feature = xidget.getFeature( ILayoutFeature.class);
      if ( feature != null) feature.setSpacing( (int)spacing);
    }
    
    return null;
  }
  
  private IExpression spacingExpr;
}
