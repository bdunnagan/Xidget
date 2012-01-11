/*
 * Xidget - XML Widgets based on JAHM
 * 
 * CreateXidgetAction.java
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

import org.xmodel.log.Log;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction which loads the xidget configuration specified by an xpath.
 * @deprecated Use XidgetAction instead.
 */
public class CreateXidgetAction extends XidgetAction
{
  /* (non-Javadoc)
   * @see org.xidget.xaction.XidgetAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    log.warn( "CreateXidgetAction is deprecated - use XidgetAction instead.");
    return super.doAction( context);
  }
  
  private static Log log = Log.getLog( CreateXidgetAction.class.getName());
}
