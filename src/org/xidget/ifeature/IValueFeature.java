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
   * Store the value taken from the xidget in the source node.
   * @return Returns true if the value was stored.
   */
  public boolean commit();
  
  /**
   * Convert the specified model value to its display value. 
   * @param value The model value.
   * @return Returns the display value.
   */
  public Object toDisplay( Object value);
  
  /**
   * Convert the specified display value to its model value. 
   * @param value The display value.
   * @return Returns the model value.
   */
  public Object toModel( Object value);
  
  /**
   * Validate the specified value. This is the value as it should appear in the model,
   * that is, without any transformation performed by the setValue( Object) method.
   * @param value The value.
   * @return Returns true if the value is valid.
   */
  public boolean validate( Object value);
  
  /**
   * @return Returns the value displayed in the xidget.
   */
  public Object getValue();
}
