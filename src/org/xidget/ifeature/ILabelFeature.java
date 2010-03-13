/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ILabelFeature.java
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

/**
 * An interface for setting the text of a label.
 */
public interface ILabelFeature
{
  /**
   * Returns the preferred width of the label.
   * @return Returns the preferred width of the label.
   */
  public int getLabelWidth();
  
  /**
   * Set the width of the label (used for pretty alignment).
   * @param width The width.
   */
  public void setLabelWidth( int width);
  
  /**
   * Set the text of the label.
   * @param text The text.
   */
  public void setLabelText( String text);
}
