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
import org.xidget.layout.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule that sets the size of a widget.
 */
public class SizeBindingRule implements IBindingRule
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
      this.bounds = new Bounds();
      this.size = new Size();
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = nodes.get( 0);
      setSize( Xlate.get( node, ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      setSize( Xlate.get( node, ""));
    }

    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      setSize( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node)
      {
        setSize( Xlate.get( node, ""));
      }
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }
    
    /**
     * Set the size of the widget.
     * @param size The size.
     */
    private void setSize( String size)
    {
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.getBounds( bounds);
      
      this.size.parse( size);
      bounds.width = this.size.width;
      bounds.height = this.size.height;
      
      feature.setBounds( bounds.x, bounds.y, this.size.width, this.size.height);
    }
    
    private IXidget xidget;
    private IModelObject node;
    private Bounds bounds;
    private Size size;
  }
}
