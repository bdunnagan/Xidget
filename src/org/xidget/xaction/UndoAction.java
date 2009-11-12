/*
 * Xidget - XML Widgets based on JAHM
 * 
 * UndoAction.java
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

import org.xidget.xaction.CommandAction.Command;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction which will undo one command from a user-defined command history.  The schema of the commands
 * on the command history are defined by the CommandAction.  The user does not need to be aware of this
 * schema.
 */
public class UndoAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    historyExpr = document.getExpression( "history", true);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject history = historyExpr.queryFirst( context);
    int index = Xlate.get( history, "index", 0);
    if ( index == 0) return null;
    
    index--;
    IModelObject entry = history.getChild( index);
    Command command = (Command)entry.getAttribute( "instance");
    if ( command.canUndo())
    {
      Xlate.set( history, "index", index);
      command.undo();
    }
    
    return null;
  }
  
  private IExpression historyExpr;
}
