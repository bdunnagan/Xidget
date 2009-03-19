/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

/**
 * An exception which is thrown when an attempt is made to set a static feature.
 */
public class StaticFeatureException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public StaticFeatureException( Object feature)
  {
    super( "The following feature is static and cannot be set: "+feature.getClass());
  }
}
