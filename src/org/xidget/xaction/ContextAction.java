/*
 * XModel (XML Application Data Modeling Framework)
 * Author: Bob Dunnagan (bdunnagan@nc.rr.com)
 * Copyright Bob Dunnagan 2009. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.NamedContexts;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction similar to the existing ContextAction that also handles named contexts.
 */
public class ContextAction extends XAction
{
  /* (non-Javadoc)
   * @see com.stonewall.cornerstone.cpmi.xaction.GuardedAction#configure(
   * com.stonewall.cornerstone.cpmi.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);

    nameExpr = document.getExpression( "name", true);
    sourceExpr = document.getExpression( "source", true);
    script = document.createScript( "source", "name");
  }

  /* (non-Javadoc)
   * @see com.stonewall.cornerstone.cpmi.xaction.IXAction#run(org.xmodel.xpath.expression.IContext)
   */
  public Object[] doRun( IContext context)
  {
    if ( nameExpr != null)
    {
      StatefulContext named = NamedContexts.get( nameExpr.evaluateString( context));
      if ( named != null) return script.run( named);
    }
    else if ( sourceExpr != null)
    {
      IModelObject source = sourceExpr.queryFirst( context);
      if ( source != null)
      {
        StatefulContext nested = new StatefulContext( context, source);
        return script.run( nested);
      }
    }
    else
    {
      StatefulContext nested = new StatefulContext( 
        context,
        context.getObject(),
        context.getPosition(),
        context.getSize());
      
      return script.run( nested);
    }
    
    return null;
  }

  private IExpression nameExpr;
  private IExpression sourceExpr;
  private ScriptAction script;
}
