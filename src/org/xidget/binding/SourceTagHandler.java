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
import org.xmodel.xpath.AttributeNode;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableListener;
import org.xmodel.xpath.variable.IVariableScope;

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
        
    // create variable binding if present
    if ( !(element instanceof AttributeNode))
    {
      String var = Xlate.get( element, "var", (String)null);
      if ( var != null)
      {
        VariableBinding binding = new VariableBinding( xidget, var);
        IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
        bindFeature.addBindingAfterChildren( binding);
      }
    }
    
    // create expression binding if present
    IExpression expression = Xlate.get( element, (IExpression)null);
    if ( expression != null)
    {
      IExpressionListener listener = new Listener( xidget);
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    
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
      ISingleValueModelFeature modelFeature = xidget.getFeature( ISingleValueModelFeature.class);
      if ( modelFeature != null) modelFeature.setSourceNode( expression.queryFirst( context));
      
      ISingleValueUpdateFeature updateFeature = xidget.getFeature( ISingleValueUpdateFeature.class);
      updateFeature.updateWidget();
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
      if ( feature != null) feature.setSourceNode( expression.queryFirst( context));
      
      ISingleValueUpdateFeature updateFeature = xidget.getFeature( ISingleValueUpdateFeature.class);
      updateFeature.updateWidget();
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
  
  private final static class VariableBinding implements IXidgetBinding, IVariableListener
  {
    public VariableBinding( IXidget xidget, String variable)
    {
      this.xidget = xidget;
      this.variable = variable;
    }

    public void bind( StatefulContext context)
    {
      ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
      feature.setSourceVariable( variable);
      
      context.getScope().addListener( variable, context, this);
    }

    public void unbind( StatefulContext context)
    {
      context.getScope().removeListener( variable, context, this);
      
      ISingleValueModelFeature feature = xidget.getFeature( ISingleValueModelFeature.class);
      feature.setSourceVariable( null);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyAdd(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyRemove(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, String newValue, String oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Number, java.lang.Number)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Number newValue, Number oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Boolean)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Boolean newValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      if ( feature != null) feature.display( newValue);
    }

    private IXidget xidget;
    private String variable;
  }
}
