/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IOptionalNodeFeature.java
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

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for handling the automatic creation and deletion of optional nodes as defined by a schema.
 */
public interface IOptionalNodeFeature
{
  /**
   * Set the source expression for the specified channel.
   * @param channel The channel.
   * @param expression The expression.
   */
  public void setSourceExpression( String channel, IExpression expression);

  /**
   * Create optional nodes based on the source expression.
   * @param channel The channel.
   * @param context The context.
   */
  public void createOptionalNodes( String channel, StatefulContext context);
  
  /**
   * Delete optional nodes based on the source expression.
   * @param channel The channel.
   * @param context The context.
   */
  public void deleteOptionalNodes( String channel, StatefulContext context);
}
