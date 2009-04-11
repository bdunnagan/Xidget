/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.binding;

import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextWidgetFeature;
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
    return new Listener( xidget, Xlate.get( element, "channel", TextXidget.allChannel));
  }
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, String channel)
    {
      this.xidget = xidget;
    }
    
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
      feature.setEditable( newValue);
    }

    private IXidget xidget;
  }
}