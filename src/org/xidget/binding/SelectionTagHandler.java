/*
 * Xidget - XML Widgets based on JAHM
 * 
 * SelectionTagHandler.java
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
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xidget.util.XidgetUtil;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableListener;
import org.xmodel.xpath.variable.IVariableScope;

/**
 * An implementation of ITagHandler for the <i>selection</i> element.
 */
public class SelectionTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature feature = parent.getFeature( IXidgetFeature.class);
    if ( feature == null) return false;
    
    IXidget xidget = feature.getXidget();
    if ( xidget == null) return false;
    
    return xidget.getFeature( ISelectionUpdateFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    
    // create variable binding if present
    String variable = Xlate.get( element, "var", Xlate.get( element, "variable", (String)null));
    if ( variable != null)
    {
      VariableBinding binding = new VariableBinding( xidget, variable);
      IBindFeature bindFeature = XidgetUtil.findTreeRoot( xidget).getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    return null;
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
      ISelectionModelFeature feature = xidget.getFeature( ISelectionModelFeature.class);
      feature.setSourceVariable( variable);
      
      context.getScope().addListener( variable, context, this);
    }

    public void unbind( StatefulContext context)
    {
      context.getScope().removeListener( variable, context, this);
      
      ISelectionModelFeature feature = xidget.getFeature( ISelectionModelFeature.class);
      feature.setSourceVariable( null);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyAdd(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displaySelect( nodes);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyRemove(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displayDeselect( nodes);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, String newValue, String oldValue)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Number, java.lang.Number)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Number newValue, Number oldValue)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Boolean)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Boolean newValue)
    {
    }

    private IXidget xidget;
    private String variable;
  }
}
