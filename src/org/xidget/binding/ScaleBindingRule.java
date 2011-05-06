/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.graph.IGraphFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of ITagHandler for defining the scales associated with a xidget.
 * A scale is a horizontal or vertical xidget with tick marks. The xidget must implement
 * the IAxisScaleFeature.
 */
public class ScaleBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IGraphFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#getListener(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  @Override
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element)
  {
    return new Listener( Xlate.get( element, "axis", (String)null), xidget);
  }
  
  private class Listener extends ExpressionListener
  {
    public Listener( String axis, IXidget xidget)
    {
      this.axis = axis;
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      Object object = node.getValue();
      if ( object != null && object instanceof IXidget)
      {
        IGraphFeature feature = xidget.getFeature( IGraphFeature.class);
        feature.setScale( axis, (IXidget)object);
      }
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      if ( node != null)
      {
        Object object = node.getValue();
        if ( object != null && object instanceof IXidget)
        {
          IGraphFeature feature = xidget.getFeature( IGraphFeature.class);
          feature.setScale( axis, (IXidget)object);
        }
      }
    }

    private String axis;
    private IXidget xidget;
  }
}
