/*
 * Xidget - XML Widgets based on JAHM
 * 
 * OpenConfirmDialogAction.java
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

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.IToolkit.Confirmation;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableScope;

/**
 * An action that opens a platform-specific confirmation dialog.
 */
public class OpenConfirmDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    variable = Xlate.get( document.getRoot(), "assign", (String)null);
    windowExpr = document.getExpression( "window", true);
    targetExpr = document.getExpression( "target", true);
    allowCancelExpr = document.getExpression( "allowCancel", true);
    titleExpr = document.getExpression( "title", true);
    imageExpr = document.getExpression( "image", true);
    messageExpr = document.getExpression( "message", true);
    if ( messageExpr == null) messageExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected Object[] doAction( IContext context)
  {
    IModelObject windowNode = windowExpr.queryFirst( context);
    if ( windowNode == null) throw new XActionException( "Window is undefined: "+windowExpr);
    
    IXidget xidget = (IXidget)windowNode.getValue();
    if ( xidget == null) throw new XActionException( "Window is undefined: "+windowExpr);

    String title = titleExpr.evaluateString( context);
    String message = messageExpr.evaluateString( context);
    boolean allowCancel = (allowCancelExpr != null)? allowCancelExpr.evaluateBoolean( context): false;
    IModelObject imageNode = (imageExpr != null)? imageExpr.queryFirst( context): null;
    Object image = (imageNode != null)? imageNode.getValue(): null;
    
    // open dialog
    IToolkit toolkit = Creator.getInstance().getToolkit();
    Confirmation confirmation = toolkit.openConfirmDialog( xidget, (StatefulContext)context, title, image, message, allowCancel);
    
    if ( targetExpr != null)
    {
      IModelObject target = targetExpr.queryFirst( context);
      if ( target != null) Xlate.set( target, confirmation.name());
    }
    
    if ( variable != null)
    {
      IVariableScope scope = context.getScope();
      if ( scope != null) scope.set( variable, confirmation.name());
    }
    
    return null;
  }

  private IExpression windowExpr;
  private IExpression targetExpr;
  private IExpression titleExpr;
  private IExpression imageExpr;
  private IExpression messageExpr;
  private IExpression allowCancelExpr;
  private String variable;
}
