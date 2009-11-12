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
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

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
    // get parent xidget
    IModelObject element = context.getObject();
    IXidget parent = (IXidget)element.getAttribute( "instance");
    
    // get parameters
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    int spacing = layoutFeature.getSpacing();
    
    // layout
    if ( xidgetsExpr == null)
    {
      if ( parent.getChildren().size() > 0)
        layout( context, parent, parent.getChildren(), spacing);
    }
    else
    {
      List<IModelObject> elements = xidgetsExpr.evaluateNodes( context);
      List<IXidget> xidgets = new ArrayList<IXidget>( elements.size());
      for( IModelObject node: elements)
      {
        IXidget xidget = (IXidget)node.getAttribute( "instance");
        if ( xidget != null) xidgets.add( xidget);
      }
      layout( context, parent, xidgets, spacing);
    }
    
    return null;
  }
  
  /**
   * Create the attachments for the children of the specified xidget.
   * @param context The context of the action.
   * @param parent The parent xidget.
   * @param children The xidget to be arranged.
   * @param spacing The spacing between xidgets.
   */
  protected abstract void layout( IContext context, IXidget parent, List<IXidget> children, int spacing);

  private IExpression xidgetsExpr;
}
