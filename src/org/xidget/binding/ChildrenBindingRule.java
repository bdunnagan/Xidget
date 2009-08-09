/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IDynamicContainerFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IBindingRule for the <i>children</i> expression in a form.
 * The <i>children</i> expression provides a dynamic form creation and deletion
 * mechanism.  The expression returns objects for which forms will be created.
 * Candidate forms are defined in a ConfigurationSwitch and chosen based on the
 * form object.  The most common application of this mechanism is tabs.
 */
public class ChildrenBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IWidgetContainerFeature.class) != null;
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
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IDynamicContainerFeature feature = xidget.getFeature( IDynamicContainerFeature.class);
      feature.setChildren( (StatefulContext)context, nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IDynamicContainerFeature feature = xidget.getFeature( IDynamicContainerFeature.class);
      feature.setChildren( (StatefulContext)context, nodes);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    private IXidget xidget;
  }
}
