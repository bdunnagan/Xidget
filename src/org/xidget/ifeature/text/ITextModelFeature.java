/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ITextModelFeature.java
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
package org.xidget.ifeature.text;

import org.xidget.ifeature.ISourceFeature;
import org.xidget.text.ITextValidator;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * The interface for setting the text of a text channel. The idea of a text widget
 * is generalized to include a text region. Text regions are defined by a string
 * that describes the channel. The following strings are reserved:
 * <ul>
 * <li><i>default</i> - The default text channel.
 * <li><i>selected</i> - The selected text channel.
 * </ul>
 */
public interface ITextModelFeature extends ISourceFeature
{
  /**
   * Set the text of the source node associated with the specified channel.
   * @param context The parent context.
   * @param channel The channel.
   * @param text The text.
   */
  public void setText( StatefulContext context, String channel, String text);
  
  /**
   * Set the expression used to transform the text before it goes into the model.
   * Note that it is the responsibility of the implementation to use this transform
   * if it is provided.
   * @param channel The channel.
   * @param transform The transform.
   */
  public void setTransform( String channel, IExpression transform);
  
  /**
   * Set the object used to validate text when it is committed to the model. The
   * validator does not prevent text from being committed, it simply reports 
   * validation errors using the IErrorAdapter.
   * @param channel The channel.
   * @param validator The validator.
   */
  public void setValidator( String channel, ITextValidator validator);
}
