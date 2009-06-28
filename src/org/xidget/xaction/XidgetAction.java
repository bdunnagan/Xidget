package org.xidget.xaction;

import org.xidget.Creator;
import org.xidget.config.TagException;
import org.xmodel.IModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction which loads the xidget configuration specified by an xpath.
 */
public class XidgetAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    sourceExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    IModelObject root = sourceExpr.queryFirst( context);
    
    try
    {
      StatefulContext inner = new StatefulContext( context, root);
      Creator creator = Creator.getInstance();
      creator.create( inner, true);
    }
    catch( TagException e)
    {
      throw new XActionException( e);
    }
  }
  
  private IExpression sourceExpr;
}
