/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TitleBindingRule.java
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
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ITitleFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
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
      ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
      feature.setTitle( Xlate.get( node, ""));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
      feature.setTitle( Xlate.get( node, ""));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
      feature.setTitle( Boolean.toString( newValue));
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
      feature.setTitle( Double.toString( newValue));
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
      feature.setTitle( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node)
      {
        ITitleFeature feature = xidget.getFeature( ITitleFeature.class);
        feature.setTitle( Xlate.get( node, ""));
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
