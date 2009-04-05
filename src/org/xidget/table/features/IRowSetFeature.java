/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.features;

import java.util.List;
import org.xmodel.IModelObject;

/**
 * An interface for performing targeted updates to the rows of a table given 
 * a complete row-set. The implementation will typically perform a shallow
 * diff of the new row-set with the old row-set.
 */
public interface IRowSetFeature
{
  /**
   * Set the new row-set of the table.
   * @param rows The row objects.
   */
  public void setRows( List<IModelObject> rows);
}
