/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IXidget.java
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
package org.xidget;

import java.util.List;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An interface for widget adapters.
 */
public interface IXidget extends IFeatured
{
  /**
   * Returns null or the parent of this xidget.
   * @return Returns null or the parent of this xidget.
   */
  public IXidget getParent();
  
  /**
   * Returns the children of this xidget.
   * @return Returns the children of this xidget.
   */
  public List<IXidget> getChildren();
  
  /**
   * Add a child to this xidget.
   * @param xidget The xidget.
   */
  public void addChild( IXidget xidget);
  
  /**
   * Add a child to this xidget at the specified index.
   * @param index The index of insertion.
   * @param xidget The xidget.
   */
  public void addChild( int index, IXidget xidget);
  
  /**
   * Remove a child xidget.
   * @param xidget The xidget.
   */
  public void removeChild( IXidget xidget);
  
  /**
   * Called when the start tag of the configuration is encountered - before children.
   * @param processor The tag processor.
   * @param parent Null or the parent of this xidget.
   * @param element The configuration element.
   * @return Returns true if the tag processor should process the children of the specified element.
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException;
  
  /**
   * Called when the end tag of the configuration is encountered - after children.
   * @param processor The tag processor.
   * @param element The configuration element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException;
  
  /**
   * Returns the configuration element that generated this xidget.
   * @return Returns the configuration element that generated this xidget.
   */
  public IModelObject getConfig();
  
  /**
   * Create the widget(s) associated with this xidget.
   */
  public void createWidget();
  
  /**
   * Destroy the widget(s) associated with this xidget.
   */
  public void destroyWidget();
}
