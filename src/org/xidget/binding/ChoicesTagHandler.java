/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ChoicesTagHandler.java
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
import org.xidget.ifeature.model.IMultiValueModelFeature;
import org.xidget.ifeature.model.IMultiValueUpdateFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of ITagHandler for the <i>choices</i> element. This element
 * can specify choices in one of two ways: using an expression, or by the elements
 * children.
 */
public class ChoicesTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    IExpression choiceExpr = getChoiceExpression( element);
    if ( choiceExpr == null) throw new TagException( "Choice list expression not defined.");
      
    // specify choice expression
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    modelFeature.setSourceExpression( choiceExpr);
    
    // bind choice expression
    IExpressionListener listener = new Listener( xidget);
    XidgetBinding binding = new XidgetBinding( choiceExpr, listener);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    if ( bindFeature != null) bindFeature.addBindingBeforeChildren( binding);
    
    return false;
  }
  
  /**
   * Returns the choice list expression given the configuration element.
   * @param element The configuration element.
   * @return Returns the choice list expression.
   */
  private IExpression getChoiceExpression( IModelObject element)
  {
    if ( element.getNumberOfChildren( "choice") > 0)
    {
      staticChoiceExpr = XPath.createExpression( "static( $choices)");
      staticChoiceExpr.setVariable( "choices", element.getChildren( "choice"));
      return staticChoiceExpr;
    }
    else
    {
      return Xlate.get( element, (IExpression)null);
    }
  }

  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IMultiValueUpdateFeature feature = xidget.getFeature( IMultiValueUpdateFeature.class);
      feature.updateWidget();
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IMultiValueUpdateFeature feature = xidget.getFeature( IMultiValueUpdateFeature.class);
      feature.updateWidget();
    }
    
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      List<IModelObject> nodes = expression.query( contexts[ 0], null);
      int index = nodes.indexOf( object);
      if ( index >= 0)
      {
        IMultiValueUpdateFeature feature = xidget.getFeature( IMultiValueUpdateFeature.class);
        feature.displayUpdate( index, object);
      }
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
     */
    @Override
    public boolean requiresValueNotification()
    {
      return true;
    }
    
    private IXidget xidget;
  }
  
  private IExpression staticChoiceExpr;
}
