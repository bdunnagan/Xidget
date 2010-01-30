/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IBindingRule.java
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

import org.xidget.IXidget;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An interface for an object which returns the listener of a XidgetBinding.
 */
public interface IBindingRule
{
  /**
   * Returns true if this rule applies to the specified xidget and configuration element.
   * @param xidget The xidget parent.
   * @param element The rule configuration element.
   * @return Returns true if this rule applies.
   */
  public boolean applies( IXidget xidget, IModelObject element);
  
  /**
   * Returns the listener which will be bound to the specified xidget. 
   * The listener must be unique for the xidget.
   * @param processor The tag processor.
   * @param xidget The xidget.
   * @param element The configuration element that created the binding.
   * @return Returns the listener which will be bound to the specified xidget.
   */
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element);
}
