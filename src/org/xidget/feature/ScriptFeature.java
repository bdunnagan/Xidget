/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ScriptFeature.java
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
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.IToolkit.MessageType;
import org.xidget.ifeature.IScriptFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.log.Log;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IScriptFeature which uses a HashMap to associate scripts with their name.
 * Before a script is run using the <code>runScript</code> method, a reserved variable is set in the
 * context.  The variable, <i>xidget</i>, holds the IXidget instance that owns the script.
 */
public class ScriptFeature implements IScriptFeature
{
  public ScriptFeature( IXidget xidget)
  {
    holder = new ModelObject( "holder");
    holder.setValue( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IScriptFeature#hasScript(java.lang.String)
   */
  @Override
  public boolean hasScript( String name)
  {
    if ( scripts == null) return false;
    return scripts.get( name) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IScriptFeature#setScript(java.lang.String, org.xmodel.xaction.ScriptAction)
   */
  public void setScript( String name, ScriptAction script)
  {
    if ( scripts == null) scripts = new HashMap<String, ScriptAction>( 5);
    scripts.put( name, script);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IScriptFeature#runScript(java.lang.String, org.xmodel.xpath.expression.StatefulContext)
   */
  public Object[] runScript( String name, StatefulContext context)
  {
    if ( scripts == null) return null;

    context.set( "xidget", holder);
    
    try
    {
      ScriptAction script = scripts.get( name);
      if ( script != null) return script.run( context);
    }
    catch( Exception e)
    {
      log.exception( e);
      
      IXidget xidget = (IXidget)holder.getValue();
      IToolkit toolkit = Creator.getInstance().getToolkit();
      toolkit.openMessageDialog( xidget, context, "Exception", null, e.getMessage(), MessageType.error);
    }
    
    return null;
  }

  private static Log log = Log.getLog( "org.xidget.feature");
  
  private IModelObject holder;
  private Map<String, ScriptAction> scripts;
}
