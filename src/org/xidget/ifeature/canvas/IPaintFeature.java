/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IPaintFeature.java
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
package org.xidget.ifeature.canvas;

/**
 * An interface for xidgets that paint themselves on their parent xidget. The parent xidget exports
 * an instance of ICanvasFeature.
 */
public interface IPaintFeature
{
  /**
   * Set the layer on which the associated xidget will be painted.
   * @param layer Null or the name of the layer.
   */
  public void setLayer( String layer);
  
  /**
   * Returns the name of the layer on which this xidget will be painted. If null is returned then
   * the xidget will be painted on the default layer. The z-order of the default layer is implementation
   * dependent.
   * @return Returns null or the name of the layer on which this xidget will be painted.
   */
  public String getLayer();
  
  /**
   * Paint the xidget to which this feature belongs using the specified graphics context.
   * @param graphics The graphics context.
   */
  public void paint( Object graphics); 
}
