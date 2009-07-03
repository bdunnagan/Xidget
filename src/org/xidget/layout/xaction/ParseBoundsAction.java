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
 * An XAction that parses a string of the form "x,y,width,height" and assigns each of these
 * values to a separate variable.  The variable names are given by a prefix with one of the
 * following suffixes: x, y, w or h.  So if the prefix was "bounds" then the variables would
 * be boundsx, boundsy, boundsw and boundsh.
 */
public class ParseBoundsAction extends GuardedAction
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
    if ( parts.length > 0) context.set( prefix+"x", parts[ 0]); else context.set( prefix+"x", "0");
    if ( parts.length > 1) context.set( prefix+"y", parts[ 1]); else context.set( prefix+"y", "0");
    if ( parts.length > 2) context.set( prefix+"w", parts[ 2]); else context.set( prefix+"w", "0");
    if ( parts.length > 3) context.set( prefix+"h", parts[ 3]); else context.set( prefix+"h", "0");
  }

  private String prefix;
  private IExpression inputExpr;
}
