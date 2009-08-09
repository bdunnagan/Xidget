package org.xidget.xaction;

import org.xidget.xaction.CommandAction.Command;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction which will undo one command from a user-defined command stack.  The schema of the commands
 * on the command stack are defined by the CommandAction.  The user does not need to be aware of this
 * schema.
 */
public class RedoAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    stackExpr = document.getExpression( "stack", true);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject stack = stackExpr.queryFirst( context);
    int index = Xlate.get( stack, "index", 0) - 1;
    if ( index < 0) return null;
    
    IModelObject entry = stack.getChild( index);
    if ( entry == null) return null;
    
    Command command = (Command)entry.getAttribute( "instance");
    Xlate.set( stack, "index", index);
    command.redo();
    
    return null;
  }
  
  private IExpression stackExpr;
}
