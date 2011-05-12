/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IScriptFeature.java
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
   * Returns true if the specified script is defined.
   * @param name The name of the script.
   * @return Returns true if the specified script is defined.
   */
  public boolean hasScript( String name);
  
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
   * @return Returns the return value of the script.
   */
  public Object[] runScript( String name, StatefulContext context);
}
