/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

import org.xmodel.IModelObject;

/**
 * An interface for getting and setting a single value in a model. This interface
 * provides methods for the tag processor to specify a storage location in the model,
 * as well as methods for getting and setting the value at that location. 
 */
public interface ISelectionModelFeature
{
  /**
   * Set the element where selection will be stored.
   * @param element The element.
   */
  public void setElementStorage( IModelObject element);
  
  /**
   * Set the name of the variable where the selection will be stored.
   * @param name The name of the variable.
   */
  public void setVariableStorage( String name);
  
  /**
   * Insert the selected object at the specified index in the selection.
   * @param index The index.
   * @param object The object.
   */
  public void insertSelected( int index, Object object);
  
  /**
   * Remove the selected object from the selection.
   * @param object The object.
   */
  public void removeSelected( Object object);
  
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
