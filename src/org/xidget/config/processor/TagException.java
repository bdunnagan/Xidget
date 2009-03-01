/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.processor;

/**
 * An exception thrown during tag processing by a tag handler which causes
 * tag processing to stop.
 */
public class TagException extends Exception
{
  private static final long serialVersionUID = 1L;

  public TagException()
  {
    super();
  }

  public TagException( String message, Throwable cause)
  {
    super( message, cause);
  }

  public TagException( String message)
  {
    super( message);
  }

  public TagException( Throwable cause)
  {
    super( cause);
  }
}
