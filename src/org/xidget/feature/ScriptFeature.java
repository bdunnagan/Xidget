/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.ifeature.IScriptFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
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
  public void runScript( String name, StatefulContext context)
  {
    if ( scripts == null) return;

    context.set( "xidget", holder);
    
    ScriptAction script = scripts.get( name);
    script.run( context);
  }

  private IModelObject holder;
  private Map<String, ScriptAction> scripts;
}
