/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

/**
 * An interface for getting and setting an ordered list of values in a widget.
 */
public interface IMultiValueWidgetFeature
{
  /**
   * Insert the specified object at the specified index.
   * @param index The index.
   * @param object The object.
   */
  public void insertValue( int index, Object object);

  /**
   * Update the value at the specified index.
   * @param index The index.
   * @param value The new value.
   */
  public void updateValue( int index, Object value);
  
  /**
   * Remove the object at the specified index.
   * @param index The index.
   */
  public void removeValue( int index);
  
  /**
   * Set the values displayed by the widget.
   * @param list The list of values.
   */
  public void setValues( List<? extends Object> list);
  
  /**
   * Get the values displayed by the widget.
   * @return Returns the list of values.
   */
  public List<? extends Object> getValues();
}
