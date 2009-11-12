/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IImageLoader.java
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
package org.xidget.platform;

import java.net.URL;
import java.util.List;

/**
 * An interface which is customized per platform for loading a platform-specific image.
 */
public interface IImageLoader
{
  /**
   * Returns the images that are available from the specified URL.
   * @param url The url where the images are located.
   * @return Returns the images that are avilable fromt he specified URL.
   */
  public List<URL> loadList( URL url);
  
  /**
   * Load an image from the specified URL.
   * @param url The url pointing to the image.
   * @return Returns a platform-specific image object.
   */
  public Object load( URL url);
}
