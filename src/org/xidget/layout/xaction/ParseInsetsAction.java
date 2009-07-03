/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

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
    String[] parts = input.split( ",");
    if ( parts.length > 0) context.set( prefix+"l", parts[ 0]); else context.set( prefix+"l", "0");
    if ( parts.length > 1) context.set( prefix+"t", parts[ 1]); else context.set( prefix+"t", "0");
    if ( parts.length > 2) context.set( prefix+"r", parts[ 2]); else context.set( prefix+"r", "0");
    if ( parts.length > 3) context.set( prefix+"b", parts[ 3]); else context.set( prefix+"b", "0");
  }

  private String prefix;
  private IExpression inputExpr;
}
