/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the widget selection.
 */
public class SelectionBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ISelectionWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#getListener(org.xidget.IXidget, org.xmodel.IModelObject)
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
      nodes.clear(); expression.query( context, nodes);
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      feature.setSelection( nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear(); expression.query( context, nodes);
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      feature.setSelection( nodes);
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      List<IModelObject> nodes = expression.query( contexts[ 0], null); 
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      feature.setSelection( nodes);
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
  }
}
