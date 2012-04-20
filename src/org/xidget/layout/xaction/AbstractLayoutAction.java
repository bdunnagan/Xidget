/*
 * Xidget - XML Widgets based on JAHM
 * 
 * AbstractLayoutAction.java
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
package org.xidget.layout.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpression.ResultType;

/**
 * A base implementation of XAction for use with the various layout actions defined in this package.
 */
public abstract class AbstractLayoutAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    xidgetsExpr = document.getExpression( "xidgets", true);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    Creator creator = Creator.getInstance();
    
    // get parent xidget
    IModelObject element = context.getObject();
    IXidget parent = creator.findXidget( element);
        
    // get parameters
    IWidgetContainerFeature containerFeature = parent.getFeature( IWidgetContainerFeature.class);
    int spacing = containerFeature.getSpacing();
    
    // layout
    if ( xidgetsExpr == null)
    {
      List<IXidget> visibles = new ArrayList<IXidget>();
      for( IXidget child: parent.getChildren())
      {
        IWidgetFeature widgetFeature = child.getFeature( IWidgetFeature.class);
        if ( widgetFeature != null && widgetFeature.getVisible()) visibles.add( child);
      }
      
      if ( parent.getChildren().size() > 0)
        layout( context, parent, visibles, spacing);
    }
    else
    {
      List<IModelObject> elements = getTargets( xidgetsExpr, context);
      List<IXidget> xidgets = new ArrayList<IXidget>( elements.size());
      for( IModelObject node: elements)
      {
        IXidget xidget = creator.findXidget( node);
        if ( xidget != null) xidgets.add( xidget);
      }
      layout( context, parent, xidgets, spacing);
    }
    
    return null;
  }
  
  /**
   * Evaluate the xidget expression and return the target xidget configuration elements.
   * @param xidgetExpr The expression that identifies the xidget configuration elements.
   * @param context The context.
   * @return Returns the list of xidget configuration elements.
   */
  protected static List<IModelObject> getTargets( IExpression xidgetExpr, IContext context)
  {
    if ( xidgetExpr.getType( context) == ResultType.NODES)
    {
      return xidgetExpr.evaluateNodes( context);
    }
    else
    {
      List<IModelObject> targets = new ArrayList<IModelObject>();
      String spec = xidgetExpr.evaluateString( context);
      String[] names = spec.split( "\\s*,\\s*");
      for( String name: names)
      {
        if ( name.length() ==  0) continue;
        xidgetFinder.setVariable( "name", name);
        targets.addAll( xidgetFinder.evaluateNodes( context));
      }
      return targets;
    }
  }
  
  /**
   * Create the attachments for the children of the specified xidget.
   * @param context The context of the action.
   * @param parent The parent xidget.
   * @param children The xidget to be arranged.
   * @param spacing The spacing between xidgets.
   */
  protected abstract void layout( IContext context, IXidget parent, List<IXidget> children, int spacing);

  private final static IExpression xidgetFinder = XPath.createExpression( "*[ @id = $name or @name = $name]");
  
  private IExpression xidgetsExpr;
}
