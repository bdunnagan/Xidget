/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

/**
 * An interface for classes which support the adapter semantic. This is really
 * a specialized implementation of the adapter pattern, so I'm calling these
 * adapters <i>features</i> instead.
 */
public interface IFeatures
{
  /**
   * Set a dynamic feature.
   * @param <T> The interface type of the feature.
   * @param clss The feature interface class.
   * @param feature The feature implementation.
   * @throws StaticFeatureException If the feature is not dynamic.
   */
  public <T> void setFeature( Class<T> clss, T feature); 
  
  /**
   * Returns null or an instance of the specified feature.
   * @param clss The interface class.
   * @return Returns null or an instance of the specified feature.
   */
  public <T> T getFeature( Class<T> clss);  
}
