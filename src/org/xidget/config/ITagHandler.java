/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ITagHandler.java
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
package org.xidget.config;

import org.xidget.IFeatured;
import org.xmodel.IModelObject;

/**
 * An interface for processing an element from an xml configuration.
 */
public interface ITagHandler extends IFeatured
{
  /**
   * Returns true if this tag handler processes the specified element. This method is
   * only called if more than one tag handler is registered for a given tag.
   * @param processor The processor.
   * @param parent The parent handler.
   * @param element The element to be processed.
   * @return Returns true if this tag handler processes the specified element.
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element);
  
  /**
   * Called when the tag processor enters an element associated with this tag handler.
   * @param processor The processor.
   * @param parent The parent handler.
   * @param element The element.
   * @return Returns true if processor should process the children of the element.
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException;
  
  /**
   * Called when the tag processor exits an element associated with this tag handler.
   * @param processor The processor.
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException;
}
