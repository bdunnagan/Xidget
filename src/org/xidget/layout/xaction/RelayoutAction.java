/*
 * Xidget - XML Widgets based on JAHM
 * 
 * RelayoutAction.java
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

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that calls the <code>IWidgetContainerFeature.relayout</code> method.
 */
public class RelayoutAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    xidgetExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    Creator creator = Creator.getInstance();

    for( IModelObject element: xidgetExpr.query( context, null))
    {
      IXidget xidget = creator.findXidget( element);
      if ( xidget != null)
      {
        IWidgetContainerFeature feature = xidget.getFeature( IWidgetContainerFeature.class);
        if ( feature != null) feature.relayout();
      }
    }
    
    return null;
  }
  
  private IExpression xidgetExpr;
}
