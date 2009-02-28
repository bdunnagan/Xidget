/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

/**
 * An interface for a text validator used by the TextProcessor class to determine
 * when an error should be published. The validator always validates text in the
 * form in which it exists in the datamodel, as opposed to the widget display form.
 */
public interface ITextValidator
{
  /**
   * Returns null or the error string for the specified text.
   * @param text The text.
   * @return Returns null or the error string for the specified text.
   */
  public String validate( String text);
}
