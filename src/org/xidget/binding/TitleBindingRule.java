/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.feature.ITitleFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>title</i> configuration element.
 */
public class TitleBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ITitleFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget.getFeature( ITitleFeature.class));
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( ITitleFeature adapter)
    {
      this.feature = adapter;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      feature.setTitle( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }
    
    private ITitleFeature feature;
  }
}
