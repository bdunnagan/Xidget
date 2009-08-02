/*
 * Stonewall Networks, Inc.
 *
 *   Project: XModelUIPlugin
 *   Author:  bdunnagan
 *   Date:    Jun 9, 2006
 *
 * Copyright 2006.  Stonewall Networks, Inc.
 */
package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.IToolkit.MessageType;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
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
    
    windowExpr = document.getExpression( "window", true);
    titleExpr = document.getExpression( "title", true);
    imageExpr = document.getExpression( "image", true);
    messageTypeExpr = document.getExpression( "type", true);
    messageExpr = document.getExpression( "message", true);
    if ( messageExpr == null) messageExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected void doAction( IContext context)
  {
    IModelObject windowNode = windowExpr.queryFirst( context);
    if ( windowNode == null) throw new XActionException( "Window is undefined: "+windowExpr);
    
    IXidget xidget = (IXidget)windowNode.getValue();
    if ( xidget == null) throw new XActionException( "Window is undefined: "+windowExpr);

    String title = titleExpr.evaluateString( context);
    String message = messageExpr.evaluateString( context);
    IModelObject imageNode = (imageExpr != null)? imageExpr.queryFirst( context): null;
    Object image = (imageNode != null)? imageNode.getValue(): null;
    String messageType = messageTypeExpr.evaluateString( context);
    
    // open dialog
    IToolkit toolkit = Creator.getInstance().getToolkit();
    toolkit.openMessageDialog( xidget, (StatefulContext)context, title, image, message, MessageType.valueOf( messageType));
  }

  private IExpression windowExpr;
  private IExpression titleExpr;
  private IExpression imageExpr;
  private IExpression messageExpr;
  private IExpression messageTypeExpr;
}
