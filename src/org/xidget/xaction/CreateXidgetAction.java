/*
 * Xidget - XML Widgets based on JAHM
 * 
 * CreateXidgetAction.java
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
package org.xidget.xaction;

import java.util.List;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.ifeature.IWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction which loads the xidget configuration specified by an xpath.
 */
public class CreateXidgetAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    contextExpr = document.getExpression( "context", true);
    parentExpr = document.getExpression( "parent", true);
    indexExpr = document.getExpression( "index", true);
    xidgetExpr = document.getExpression( "xidget", true);
    if ( xidgetExpr == null) xidgetExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    Creator creator = Creator.getInstance();
    
    IModelObject root = xidgetExpr.queryFirst( context);
    if ( root == null) throw new XActionException( "Xidget not found at: "+xidgetExpr);

    try
    {
      StatefulContext configContext = new StatefulContext( context, root);
      StatefulContext bindContext = createBindContext( context, configContext);
      IModelObject parentNode = (parentExpr != null)? parentExpr.queryFirst( context): null;
      IXidget parent = creator.findXidget( parentNode);
      int index = (indexExpr != null)? (int)indexExpr.evaluateNumber( context): -1;
      List<IXidget> xidgets = creator.create( parent, index, configContext, bindContext);
      for( IXidget xidget: xidgets) 
      {
        IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
        if ( widgetFeature != null) widgetFeature.setVisible( true);
      }
    }
    catch( TagException e)
    {
      throw new XActionException( e);
    }
    
    return null;
  }

  /**
   * Create the context in which the xidget will be bound. The configuration context will be its parent.
   * @param context The xaction context.
   * @param configContext The configuration context.
   * @return Returns the new binding context.
   */
  private StatefulContext createBindContext( IContext context, StatefulContext configContext)
  {
    if ( contextExpr != null)
    {
      IModelObject node = contextExpr.queryFirst( context);
      if ( node != null) return new StatefulContext( configContext, node);
    }
    return configContext;
  }
  
  private IExpression contextExpr;
  private IExpression parentExpr;
  private IExpression indexExpr;
  private IExpression xidgetExpr;
}
