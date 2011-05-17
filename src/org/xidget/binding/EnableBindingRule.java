/*
 * Xidget - XML Widgets based on JAHM
 * 
 * EnableBindingRule.java
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
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * The binding rule for the <i>enable</i> configuration element.
 */
public class EnableBindingRule implements IBindingRule
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
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      setEnabled( true);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      setEnabled( expression.query( context, null));
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      setEnabled( newValue);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      setEnabled( newValue);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, boolean)
     */
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      setEnabled( newValue);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
     */
    @Override
    public boolean requiresValueNotification()
    {
      return false;
    }

    private void setEnabled( List<IModelObject> nodes)
    {
      setEnabled( nodes.size() > 0);
    }
    
    private void setEnabled( String string)
    {
      if ( string.equals( "true")) setEnabled( true);
      else if ( string.equals( "false")) setEnabled( false);
    }
    
    private void setEnabled( double value)
    {
      setEnabled( value != 0);
    }
    
    private void setEnabled( boolean value)
    {
      IWidgetFeature feature = xidget.getFeature( IWidgetFeature.class);
      feature.setEnabled( value);
    }
    
    private IXidget xidget;
  }
}
