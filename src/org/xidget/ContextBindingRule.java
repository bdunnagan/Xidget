/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An IBindingRule which listens for context changes specified by the expression
 * in a <i>context</i> element.  This element can appear as the child of any 
 * xidget.
 */
public class ContextBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }

  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      StatefulContext context2 = new StatefulContext( xidget.getContext().getParent(), nodes.get( 0));
      xidget.setContext( context2);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear();
      expression.query( context, nodes);
      if ( nodes.size() > 0) 
      {
        StatefulContext context2 = new StatefulContext( xidget.getContext().getParent(), nodes.get( 0));
        xidget.setContext( context2);
      }
    }
    
    private IXidget xidget;
  }
}
