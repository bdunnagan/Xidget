/*
 * Xidget - XML Widgets based on JAHM
 * 
 * SourceTagHandler.java
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
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of ITagHandler for the <i>source</i> element.
 */
public class SourceTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature feature = parent.getFeature( IXidgetFeature.class);
    if ( feature == null) return false;
    
    IXidget xidget = feature.getXidget();
    return xidget != null && xidget.getFeature( ISingleValueModelFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    IXidget xidget = xidgetFeature.getXidget();
        
    // create source expression
    IExpression expression = Xlate.get( element, (IExpression)null);
    
    // create binding
    IExpressionListener listener = new Listener( xidget);
    XidgetBinding binding = new XidgetBinding( expression, listener);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.addBindingAfterChildren( binding);
    
    return true;
  }
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
      if ( feature != null) feature.setSourceNode( expression.queryFirst( context));
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
      if ( feature != null) feature.setSourceNode( expression.queryFirst( context));
    }

    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }

    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }
    
    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
  }
}
