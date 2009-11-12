/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IXidgetBinding.java
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

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for things that are bound when a xidget is bound. Each instance is owned
 * by a single xidget and its bind and unbind methods are called when the xidget bind
 * and unbind methods are called.
 */
public interface IXidgetBinding
{
  /**
   * Bind the listener to the expression in the specified context.
   * @param context The context.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind the listener from the expression in the specified context.
   * @param context The context.
   */
  public void unbind( StatefulContext context);
}
