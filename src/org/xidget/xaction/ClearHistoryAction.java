package org.xidget.xaction;

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
public class ClearHistoryAction extends GuardedAction
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
    Xlate.set( history, "index", 0);
    history.removeChildren();
    return null;
  }
  
  private IExpression historyExpr;
}
