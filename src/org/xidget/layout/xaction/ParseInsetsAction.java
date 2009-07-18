/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import org.xidget.layout.Margins;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that parses a string of the form "left,top,right,bottom" and assigns each of these
 * values to a separate variable.  The variable names are given by a prefix with one of the
 * following suffixes: l, t, r or b.  So if the prefix was "margin" then the variables would
 * be marginl, margint, marginr and marginb.
 */
public class ParseInsetsAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    prefix = Xlate.get( document.getRoot(), "prefix", "");
    inputExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    String input = inputExpr.evaluateString( context);
    Margins margins = new Margins( input);
    context.set( prefix+"l", margins.x0);
    context.set( prefix+"t", margins.y0);
    context.set( prefix+"r", margins.x1);
    context.set( prefix+"b", margins.y1);
  }

  private String prefix;
  private IExpression inputExpr;
}
