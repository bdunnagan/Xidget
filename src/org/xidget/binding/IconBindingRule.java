/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.feature.IIconFeature;
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
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IIconFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget.getFeature( IIconFeature.class));
  }  

  private static final class Listener extends ExpressionListener
  {
    Listener( IIconFeature feature)
    {
      this.feature = feature;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = nodes.get( 0);
      feature.setIcon( node.getValue());
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      feature.setIcon( (node == null)? null: node.getValue()); 
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node) feature.setIcon(  newValue);
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IIconFeature feature;
    private IModelObject node;
  }
}
