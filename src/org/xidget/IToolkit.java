/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.TagProcessor;
import org.xmodel.external.ICachingPolicy;

/**
 * An interface for platform-specific behavior.
 */
public interface IToolkit
{
  /**
   * Configure the specified tag processor for parsing a xidget configuration file for a specific platform.
   * The implementation should add whatever tag handlers are required to generate platform-specific
   * xidget implementations.
   * @param processor The tag processor.
   */
  public void configure( TagProcessor processor);
  
  /**
   * Returns an implementation of ICachingPolicy for caching images.
   * @return Returns an implementation of ICachingPolicy for caching images.
   */
  public ICachingPolicy getImageCachingPolicy();
}
