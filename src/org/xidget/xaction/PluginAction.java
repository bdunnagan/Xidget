/*
 * Xidget - XML Widgets based on JAHM
 * 
 * PluginAction.java
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
import org.xidget.IPlugin;
import org.xidget.IToolkit;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An XAction that identifies a plugin class to be loaded and configured. After this action
 * is executed, the xidgets and services of the plugin will be permanently available.
 */
public class PluginAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    pluginExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @SuppressWarnings("rawtypes")
  @Override
  protected Object[] doAction( IContext context)
  {
    String className = pluginExpr.evaluateString( context);
    try
    {
      Class clss = getDocument().getClassLoader().loadClass( className);
      IPlugin plugin = (IPlugin)clss.newInstance();
      IToolkit toolkit = Creator.getInstance().getToolkit();
      if ( toolkit == null) throw new XActionException( "Toolkit must be defined before plugins can be loaded.");
      plugin.configure( toolkit);
    }
    catch( Exception e)
    {
      throw new XActionException( "Unable to load implementation of IPlugin: "+className, e);
    }
    
    return null;
  }
  
  private IExpression pluginExpr;
}
