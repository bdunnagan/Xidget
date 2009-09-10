/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.function.FunctionFactory;
import org.xmodel.xpath.function.custom.DelegateFunction;

/**
 * An XAction that creates instances of DelegateFunction.
 */
public class DeclareAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject element = getDocument().getRoot();
    String name = Xlate.get( element, "name", (String)null);
    String spec = Xlate.get( element, (String)null);
    if ( name != null && spec != null)
    {
      DelegateFunction function = new DelegateFunction( name, spec);
      FunctionFactory.getInstance().register( name, function);
    }
    return null;
  }
}
