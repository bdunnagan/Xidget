/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.xaction.ScriptAction;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for managing and executing xidget scripts. A xidget may be associated with
 * any number of scripts. Script are identified by name. A script name is given by the 
 * element type containing the script, or by the <i>name</i> attribute.  In either case
 * it is a string, and scripts are accessed from this feature using that string.
 */
public interface IScriptFeature
{
  /**
   * Set the script with the specified name.
   * @param name The name of the script.
   * @param script The script.
   */
  public void setScript( String name, ScriptAction script);
  
  /**
   * Execute the script with the specified name, if it exists. 
   * This method does nothing if the script does not exist.
   * @param name The name of the script.
   * @param context The context in which to execute the script.
   */
  public void runScript( String name, StatefulContext context);
}
