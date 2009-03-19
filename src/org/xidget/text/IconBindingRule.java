/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import java.util.List;
import org.xidget.IBindingRule;
import org.xidget.IXidget;
import org.xidget.text.feature.IIconFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule for icons.
 */
public class IconBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( (IIconFeature)xidget.getFeature( IIconFeature.class));
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( IIconFeature feature)
    {
      this.feature = feature;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      feature.setIcon( nodes.get( 0).getValue());
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes.clear();
      expression.query( context, nodes);
      if ( nodes.size() > 0) feature.setIcon( nodes.get( 0).getValue()); 
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IIconFeature feature;
  }
}
