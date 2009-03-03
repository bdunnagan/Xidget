/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

/**
 * An interface for validating text when it is committed to the model.
 */
public interface ITextValidator
{
  /**
   * Validate the specified text and return null or a validation error string.
   * @param text The text to be validated.
   * @return Returns null or the validation error string.
   */
  public String validate( String text);
}
