/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.Bounds;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule that sets the bounds of a xidget. The bounds can be defined
 * in a node, in which case the node will be updated when the bounds are changed, or in a string.
 */
public class BoundsBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#getListener(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element)
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
      node = nodes.get( 0);
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setBoundsNode( node);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setBoundsNode( node);
    }

    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      setBounds( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node)
      {
        setBounds( Xlate.get( node, ""));
      }
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }
    
    /**
     * Set the bounds of the widget.
     * @param string The bounds string.
     */
    private void setBounds( String string)
    {
      Bounds bounds = new Bounds();
      
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.getBounds( bounds);
      
      if ( bounds.parse( string))
      {
        feature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
      }
    }
    
    private IXidget xidget;
    private IModelObject node;
  }
}
