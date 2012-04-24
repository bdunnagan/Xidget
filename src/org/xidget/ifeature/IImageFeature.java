/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IIconFeature.java
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
 * An interface for specifying an images to a xidget.
 */
public interface IImageFeature
{
  /**
   * Set the image.
   * @param image The image.
   */
  public void setImage( Object image);
  
  /**
   * Set the mouse-hovering image (if supported).
   * @param image The image.
   */
  public void setImageHover( Object image);
  
  /**
   * Set the mouse-pressed image (if supported).
   * @param image The image.
   */
  public void setImagePress( Object image);
}
