/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for getting and setting a single value in a model. This interface
 * provides methods for the tag processor to specify a storage location in the model,
 * as well as methods for getting and setting the value at that location. 
 */
public interface ISelectionModelFeature
{
  /**
   * Set the element where selection will be stored.
   * @param context The current evaluation context.
   * @param element The element.
   */
  public void setElementStorage( StatefulContext context, IModelObject element);
  
  /**
   * Set the name of the variable where the selection will be stored.
   * @param context The current evaluation context.
   * @param name The name of the variable.
   */
  public void setVariableStorage( StatefulContext context, String name);
  
  /**
   * Set the value in the storage location. This method has no effect if the 
   * storage location has not yet been defiend.
   * @param context The current evaluation context.
   * @param value The value.
   * @return Returns the previous value.
   */
  public Object setSelection( StatefulContext context, Object value);
  
  /**
   * @param context The current evaluation context.
   * @return Returns null or the value in the storage location.
   */
  public Object getValue( StatefulContext context);
}
