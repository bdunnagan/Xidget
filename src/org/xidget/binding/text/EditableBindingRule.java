/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding.text;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.feature.text.TextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>enable</i> configuration element.
 */
public class EditableBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( ITextWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget, Xlate.get( element, "channel", TextModelFeature.allChannel));
  }
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
      feature.setEditable( expression.evaluateBoolean( context));
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
      feature.setEditable( expression.evaluateBoolean( context));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
      feature.setEditable( newValue);
    }

    private IXidget xidget;
  }
}