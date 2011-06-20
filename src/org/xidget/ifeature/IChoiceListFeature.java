/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IChoiceListFeature.java
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

/**
 * An interface for xidgets which provide a list of choices from which the
 * client can choose (i.e. a combo box).
 */
public interface IChoiceListFeature
{
  /**
   * Returns a copy of the list of choices.
   * @return Returns a copy of the list of choices.
   */
  public List<Object> getChoices();

  /**
   * Add a choice to the end of the list.
   * @param choice The choice.
   */
  public void addChoice( Object choice);
  
  /**
   * Insert a choice in the middle of the list.
   * @param index The insert index.
   * @param choice The choice.
   */
  public void insertChoice( int index, Object choice);
  
  /**
   * Remove a choice.
   * @param choice The choice.
   */
  public void removeChoice( Object choice);
  
  /**
   * Remove the choice at the specified index.
   * @param index The index.
   */
  public void removeChoice( int index);
  
  /**
   * Remove all the choices.
   */
  public void removeAllChoices();

  /**
   * Update the specified choice.
   * @param index The index of the choice.
   * @param choice The updated choice.
   */
  public void updateChoice( int index, Object choice);
}
