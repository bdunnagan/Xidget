/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for getting and setting the value displayed by a xidget.
 */
public interface IValueFeature
{
  /**
   * Set the value to be displayed by the xidget. This is the value taken directly from the model. 
   * Implementations may transform the value before it is displayed.
   * @param value The value.
   */
  public void display( Object value);
  
  /**
   * Store the specified value taken from the xidget in the source node.
   * @param value The value currently displayed in the xidget.
   * @return Returns true if the value was stored.
   */
  public boolean commit( Object value);
  
  /**
   * Validate the specified value. This is the value as it should appear in the model,
   * that is, without any transformation performed by the setValue( Object) method.
   * @param value The value.
   * @return Returns true if the value is valid.
   */
  public boolean validate( Object value);
}
