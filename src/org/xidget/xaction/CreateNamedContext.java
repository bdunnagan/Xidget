/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import org.xidget.NamedContexts;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction that creates a named context. An optional parent context may be specified
 * by giving the name of the parent context. An optional list of variable declarations
 * may also be provided.
 */
public class CreateNamedContext extends GuardedAction
{
  public final static String contextPathVariable = "named.context.path";
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);

    nameExpr = document.getExpression( "name", true);
    parentExpr = document.getExpression( "parent", true);
    contextExpr = document.getExpression( "context", true);
    contextSpec = Xlate.childGet( document.getRoot(), "context", (String)null);
    variablesExpr = document.getExpression( "variables", true);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    String name = nameExpr.evaluateString( context);
    String parentName = parentExpr.evaluateString( context);
    StatefulContext parent = NamedContexts.get( parentName);
    
    IModelObject node = contextExpr.queryFirst( context);
    if ( parent != null)
    {
      StatefulContext newContext = new StatefulContext( parent, node);
      newContext.set( contextPathVariable, contextSpec);
      createVariables( context, newContext);
      NamedContexts.set( name, newContext);
    }
    else
    {
      StatefulContext newContext = new StatefulContext( node);
      newContext.set( contextPathVariable, contextSpec);
      createVariables( context, newContext);
      NamedContexts.set( name, newContext);
    }
    
    return null;
  }
  
  /**
   * Create variables according to the configuration on the specified named context.
   * @param context The context of the xaction.
   * @param named The named context.
   */
  private void createVariables( IContext context, StatefulContext named)
  {
    for( IModelObject declaration: variablesExpr.query( context, null))
    {
      String name = Xlate.get( declaration, "name", (String)null);
      IExpression expression = Xlate.get( declaration, (IExpression)null);
      if ( expression != null)
      {
        switch( expression.getType( context))
        {
          case NODES:   named.set( name, expression.evaluateNodes( context)); break;
          case STRING:  named.set( name, expression.evaluateString( context)); break;
          case NUMBER:  named.set( name, expression.evaluateNumber( context)); break;
          case BOOLEAN: named.set( name, expression.evaluateBoolean( context)); break;
        }
      }
    }
  }

  private IExpression nameExpr;
  private IExpression parentExpr;
  private IExpression contextExpr;
  private String contextSpec;
  private IExpression variablesExpr;
}
