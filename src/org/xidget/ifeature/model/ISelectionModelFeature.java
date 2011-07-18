/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

/**
 * An interface for getting and setting a single value in a model. This interface
 * provides methods for the tag processor to specify a storage location in the model,
 * as well as methods for getting and setting the value at that location. 
 */
public interface ISelectionModelFeature
{
  /**
   * Set the name of a context variable where the selection will be stored.
   * @param name The name of the variable.
   */
  public void setSourceVariable( String name);
  
  /**
   * Add the specified object to the selection.
   * @param object The object.
   */
  public void select( Object object);

  /**
   * Remove the specified object for the selection.
   * @param object The object.
   */
  public void deselect( Object object);
  
  /**
   * Set the list of selected objects.
   * @param list The list of selected objects.
   */
  public void setSelection( List<? extends Object> list);
  
  /**
   * @return Returns the list of selected objects.
   */
  public List<? extends Object> getSelection();
}
