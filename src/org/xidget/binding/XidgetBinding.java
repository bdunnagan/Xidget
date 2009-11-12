/*
 * Xidget - XML Widgets based on JAHM
 * 
 * XidgetBinding.java
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
package org.xidget.binding;

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A class which manages the details of xpath expression bindings for xidgets.
 */
public class XidgetBinding implements IXidgetBinding
{
  /**
   * Create a binding with the default notification scheme: <i>bind</i>.
   * @param expression The expression.
   * @param listener The listener.
   */
  public XidgetBinding( IExpression expression, IExpressionListener listener)
  {
    this.expression = expression;
    this.listener = listener;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.binding.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void bind( StatefulContext context)
  {
    expression.addNotifyListener( context, listener);
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void unbind( StatefulContext context)
  {
    expression.removeListener( context, listener);
  }
  
  private IExpression expression;
  private IExpressionListener listener;
}
