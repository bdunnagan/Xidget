/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TimerAction.java
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

import org.xidget.Creator;
import org.xidget.ifeature.IAsyncFeature;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.IXAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction which executes another action periodically with a specified delay.
 */
public class StartTimerAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    idExpr = document.getExpression( "id", true);
    delayExpr = document.getExpression( "delay", true);
    repeatExpr = document.getExpression( "repeat", true);
    script = document.createScript( "delay", "repeat");
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    String id = (idExpr != null)? idExpr.evaluateString( context): Integer.toString( hashCode());
    if ( id.length() == 0) throw new XActionException( getDocument(), "Unable to start timer with empty identifier.");
    
    int period = (int)delayExpr.evaluateNumber( context);
    boolean repeat = (repeatExpr != null)? repeatExpr.evaluateBoolean( context): false;
    
    IAsyncFeature feature = Creator.getToolkit().getFeature( IAsyncFeature.class);
    if ( feature != null)
    {
      Task task = new Task( context);
      feature.schedule( id, period, repeat, task);
    }
      
    return null;
  }
  
  private class Task implements Runnable
  {
    public Task( IContext context)
    {
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
      script.run( context);
    }
    
    private IContext context;
  }

  private IExpression idExpr;
  private IExpression delayExpr;
  private IExpression repeatExpr;
  private IXAction script;
}
