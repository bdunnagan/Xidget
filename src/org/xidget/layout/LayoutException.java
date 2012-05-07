/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An exception that is thrown when a layout cannot be processed to completion.
 */
@SuppressWarnings("serial")
public class LayoutException extends RuntimeException
{
  public LayoutException( String message)
  {
    super( message);
  }
}
