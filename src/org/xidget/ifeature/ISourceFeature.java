/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ISourceFeature.java
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

import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for accessing the source element(s) of a xidget. The source element
 * stores the textual content of a xidget.  The source element is kept up-to-date
 * with the content according to the read/write semantics of the xidget.  For example,
 * a text xidget may either update the source element when a character is typed, or
 * after the enter key is pressed.
 * <p>
 * Some xidgets may have more than one source element, in which case the <i>channel</i>
 * name distinguishes them.
 */
public interface ISourceFeature
{
  public final static String allChannel = "all";
  public final static String selectedChannel = "selected";
  
  /**
   * Set the source node of the specified channel.
   * @param context The parent context.
   * @param channel The source channel.
   * @param node The source node.
   */
  public void setSource( StatefulContext context, String channel, IModelObject node);
  
  /**
   * Get the source node of the specified channel.
   * @return Returns null or the source node of the specified channel.
   */
  public IModelObject getSource( String channel);
}
