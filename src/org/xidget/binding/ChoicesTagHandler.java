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

import java.util.ArrayList;
import java.util.List;

import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IChoiceListFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.diff.AbstractListDiffer;
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

    //
    // This tag handler does not handle statically defined choices. So there is nothing to do if the
    // element has children (static choices).  Static choices are handled in the IWidgetCreationFeature.
    //
    IXidget xidget = xidgetFeature.getXidget();
    if ( element.getNumberOfChildren( "choice") == 0)
    {
      // choice list expression
      IExpression expression = Xlate.get( element, Xlate.childGet( element, "list", (IExpression)null));
      if ( expression == null) throw new TagException( "Choice list expression not defined.");
      
      // bind choices expression
      IExpressionListener listener = new Listener( xidget);
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      if ( bindFeature != null) bindFeature.addBindingBeforeChildren( binding);
      
      // choice display transform
      IExpression transform = Xlate.childGet( element, "show", (IExpression)null);
      if ( transform != null)
      {
        IChoiceListFeature choiceListFeature = xidget.getFeature( IChoiceListFeature.class);
        choiceListFeature.setTransform( transform);
      }
    }
    
    return false;
  }

  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.differ = new ListDiffer();
      this.choiceListFeature = xidget.getFeature( IChoiceListFeature.class);
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      updateChoices( context, nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      updateChoices( context, nodes);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
     */
    @Override
    public boolean requiresValueNotification()
    {
      return false;
    }

//    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
//    {
//      List<IModelObject> nodes = expression.query( contexts[ 0], null);
//      int index = nodes.indexOf( object);
//      if ( index >= 0) choiceListFeature.updateChoice( index, object);
//    }

    private void updateChoices( IContext parent, List<IModelObject> rhs)
    {
      List<Object> lhsCopy = new ArrayList<Object>( choiceListFeature.getChoices());
      List<Object> rhsCopy = new ArrayList<Object>( rhs);
      differ.diff( lhsCopy, rhsCopy);
    }
    
    @SuppressWarnings("rawtypes")
    private class ListDiffer extends AbstractListDiffer
    {
      /* (non-Javadoc)
       * @see org.xmodel.diff.IListDiffer#notifyInsert(java.util.List, int, int, java.util.List, int, int)
       */
      public void notifyInsert( List lhs, int lIndex, int lAdjust, List rhs, int rIndex, int rCount)
      {
        int index = lIndex + lAdjust;
        for( int i=0; i<rCount; i++, index++) choiceListFeature.insertChoice( index, rhs.get( rIndex + i));
      }

      /* (non-Javadoc)
       * @see org.xmodel.diff.IListDiffer#notifyRemove(java.util.List, int, int, java.util.List, int)
       */
      public void notifyRemove( List lhs, int lIndex, int lAdjust, List rhs, int rCount)
      {
        for( int i=0; i<rCount; i++) choiceListFeature.removeChoice( lIndex + lAdjust);
      }
    }
    
    private ListDiffer differ;
    private IChoiceListFeature choiceListFeature;
  }
}
