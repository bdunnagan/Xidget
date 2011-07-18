/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

import org.xmodel.xpath.expression.IExpression;

/**
 * An interface for getting and setting an ordered list of values in a model.
 * This interface provides methods for the tag processor to specify a storage 
 * location in the model, as well as methods for getting and setting the values 
 * at that location. 
 */
public interface IMultiValueModelFeature
{
  /**
   * Set the expression whose node-set defines the values of the model (read-only).
   * @param expression The expression.
   */
  public void setSourceExpression( IExpression expression);
  
  /**
   * Set the name of a context variable where the values are stored.
   * @param name The name of the variable.
   */
  public void setSourceVariable( String name);
  
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
   * Set the values displayed by the model.
   * @param list The list of values.
   */
  public void setValues( List<? extends Object> list);
  
  /**
   * Get the values displayed by the model.
   * @return Returns the list of values.
   */
  public List<? extends Object> getValues();
}
