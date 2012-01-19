/*
 * Xidget - XML Widgets based on JAHM
 * 
 * OpenMessageDialogAction.java
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
import org.xidget.IToolkit.MessageType;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An action that opens a platform-specific confirmation dialog.
 */
public class OpenMessageDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    titleExpr = document.getExpression( "title", true);
    imageExpr = document.getExpression( "image", true);
    messageTypeExpr = document.getExpression( "type", true);
    messageExpr = document.getExpression( "message", true);
    if ( messageExpr == null) messageExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected Object[] doAction( IContext context)
  {
    String title = titleExpr.evaluateString( context);
    String message = messageExpr.evaluateString( context);
    IModelObject imageNode = (imageExpr != null)? imageExpr.queryFirst( context): null;
    Object image = (imageNode != null)? imageNode.getValue(): null;
    String messageType = messageTypeExpr.evaluateString( context);
    
    // open dialog
    IToolkit toolkit = Creator.getToolkit();
    toolkit.openMessageDialog( (StatefulContext)context, title, image, message, MessageType.valueOf( messageType));
    
    return null;
  }

  private IExpression titleExpr;
  private IExpression imageExpr;
  private IExpression messageExpr;
  private IExpression messageTypeExpr;
}
