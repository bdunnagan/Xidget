/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import org.xmodel.IModelObject;

/**
 * An interface for getting and setting a single value in a model. This interface
 * provides methods for the tag processor to specify a storage location in the model,
 * as well as methods for getting and setting the value at that location. 
 */
public interface ISingleValueModelFeature
{
  /**
   * Set the node that stores the value.
   * @param node The node.
   */
  public void setStorageLocation( IModelObject node);
  
  /**
   * Set the value in the storage location. This method has no effect if the 
   * storage location has not yet been defiend.
   * @param value The value.
   */
  public void setValue( Object value);
  
  /**
   * @return Returns null or the value in the storage location.
   */
  public Object getValue();
}
