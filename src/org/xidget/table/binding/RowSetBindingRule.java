/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.table.features.IRowSetFeature;
import org.xidget.table.features.ITableModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the row-set of a table.
 */
public class RowSetBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ITableModelFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      rowSetFeature = xidget.getFeature( IRowSetFeature.class);
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      rowSetFeature.setRows( nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      rowSetFeature.setRows( nodes);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    private IRowSetFeature rowSetFeature;
  }
}
