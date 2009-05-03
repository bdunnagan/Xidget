/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
