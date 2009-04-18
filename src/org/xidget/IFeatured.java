/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

/**
 * An interface for classes which support the feature semantic. This is really
 * a specialized implementation of the adapter pattern, so I'm calling these
 * adapters <i>features</i> instead.
 */
public interface IFeatured
{
  /**
   * Returns null or an instance of the specified feature.
   * @param clss The interface class.
   * @return Returns null or an instance of the specified feature.
   */
  public <T> T getFeature( Class<T> clss);  
}
