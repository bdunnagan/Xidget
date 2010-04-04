/*
 * Xidget - XML Widgets based on JAHM
 * 
 * FontSizeBindingRule.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.binding.slider;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.slider.ISliderWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule for ISliderFeature maximum.
 */
public class MaximumBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
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
      ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
      feature.setMaximum( Xlate.get( node, 0d));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
      feature.setMaximum( Xlate.get( node, 0d));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      try
      {
        ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
        feature.setMaximum( Double.parseDouble( newValue));
      }
      catch( Exception e)
      {
      }
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
      feature.setMaximum( newValue);
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node) 
      {
        ISliderWidgetFeature feature = xidget.getFeature( ISliderWidgetFeature.class);
        feature.setMaximum( Xlate.get( object, 0d));
      }
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
    private IModelObject node;
  }
}
