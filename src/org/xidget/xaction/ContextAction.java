/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ContextAction.java
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

import org.xidget.NamedContexts;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.expression.IExpression.ResultType;

/**
 * An XAction similar to the existing ContextAction that also handles named contexts.
 */
public class ContextAction extends XAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.XAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);

    sourceExpr = document.getExpression( "source", true);
    script = document.createScript( "source", "name");
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.XAction#doRun(org.xmodel.xpath.expression.IContext)
   */
  public Object[] doRun( IContext context)
  {
    if ( sourceExpr != null)
    {
      if ( sourceExpr.getType( context) == ResultType.STRING)
      {
        StatefulContext named = NamedContexts.get( sourceExpr.evaluateString( context));
        if ( named != null) return script.run( named);
      }
      else
      {
        IModelObject source = sourceExpr.queryFirst( context);
        if ( source != null)
        {
          StatefulContext nested = new StatefulContext( context, source);
          return script.run( nested);
        }
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

  private IExpression sourceExpr;
  private ScriptAction script;
}
