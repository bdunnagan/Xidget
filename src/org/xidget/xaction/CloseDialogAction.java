/*
 * Xidget - XML Widgets based on JAHM
 * 
 * CloseDialogAction.java
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

import org.xidget.IXidget;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction that closes a dialog.
 */
public class CloseDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject holder = xidgetExpr.queryFirst( context);
    if ( holder == null) return null;
    
    IXidget xidget = (IXidget)holder.getValue();
    IDialogFeature feature = xidget.getFeature( IDialogFeature.class);
    feature.close( (StatefulContext)context);
    
    return null;
  }
  
  private final IExpression xidgetExpr = XPath.createExpression( "$dialog"); 
}
