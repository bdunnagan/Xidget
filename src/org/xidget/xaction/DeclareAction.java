/*
 * Xidget - XML Widgets based on JAHM
 * 
 * DeclareAction.java
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

import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.function.FunctionFactory;
import org.xmodel.xpath.function.custom.DelegateFunction;

/**
 * An XAction that creates instances of DelegateFunction.
 */
public class DeclareAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject element = getDocument().getRoot();
    String name = Xlate.get( element, "name", (String)null);
    String spec = Xlate.get( element, (String)null);
    if ( name != null && spec != null)
    {
      DelegateFunction function = new DelegateFunction( name, spec);
      FunctionFactory.getInstance().register( name, function);
    }
    return null;
  }
}
