/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IBindFeature.java
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

import java.util.List;
import org.xidget.binding.IXidgetBinding;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the binding feature of a xidget.
 */
public interface IBindFeature
{
  /**
   * Add a binding which will be performed before children are bound.
   * @param binding The binding.
   */
  public void addBindingBeforeChildren( IXidgetBinding binding);
  
  /**
   * Add a binding which will be performed after children are bound.
   * @param binding The binding.
   */
  public void addBindingAfterChildren( IXidgetBinding binding);
  
  /**
   * Remove a binding.
   * @param binding The binding.
   */
  public void remove( IXidgetBinding binding);
  
  /**
   * Bind to the specified context.
   * @param context The context.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind from the specified context.
   * @param context The context.
   */
  public void unbind( StatefulContext context);

  /**
   * Returns the list of currently bound contexts.
   * @return Returns the list of currently bound contexts.
   */
  public List<StatefulContext> getBoundContexts();
  
  /**
   * Returns the singular context that was bound. If more than one context has been
   * bound then this method throws an exception. Most xidgets are only ever bound
   * to one context. These xidgets have features that are dependent on this property.
   * @return Returns null or the singular context that was bound.
   */
  public StatefulContext getBoundContext();
}
