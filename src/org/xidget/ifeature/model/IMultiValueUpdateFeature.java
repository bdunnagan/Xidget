/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

/**
 * This interface defines the update semantics for a widget with multiple values.
 */
public interface IMultiValueUpdateFeature
{
  /**
   * Update the widget values based on the values in the model. This method retrieves
   * the list of values from the widget and the model and determines what objects need
   * to be inserted and removed to synchronize them.
   */
  public void updateWidget();
  
  /**
   * Update the model values based on the values in the widget. This method retrieves
   * the list of values from the widget and the model and determines what objects need
   * to be inserted and removed to synchronize them.
   */
  public void updateModel();

  /**
   * Insert the specified value into the list of values displayed by the widget at the specified index.
   * @param index The index.
   * @param value The value.
   */
  public void displayInsert( int index, Object value);
  
  /**
   * Remove the value at the specified index from the widget.
   * @param index The index.
   */
  public void displayRemove( int index);
  
  /**
   * Update the value at the specified index in the widget.
   * @param index The index.
   * @param value The value.
   */
  public void displayUpdate( int index, Object value);
  
  /**
   * Insert the specified value at the specified index in the model.
   * @param index The index.
   * @param value The value.
   */
  public void modelInsert( int index, Object value);
  
  /**
   * Remove the value at the specified index in the model.
   * @param index The index.
   */
  public void modelRemove( int index);
  
  /**
   * Update the value at the specified index in the model.
   * @param index The index.
   * @param value The value.
   */
  public void modelUpdate( int index, Object value);
}
