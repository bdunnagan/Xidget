/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for performing targeted updates to the rows of a table given 
 * a complete row-set. The implementation will typically perform a shallow
 * diff of the new row-set with the old row-set.
 */
public interface IRowSetFeature
{
  /**
   * Set the rows of the row-set.
   * @param context The parent context.
   * @param rows The row objects.
   */
  public void setRows( StatefulContext context, List<IModelObject> rows);
  
  /**
   * Returns the rows of the row-set.
   * @param context The parent context.
   * @return Returns the rows of the row-set.
   */
  public List<IModelObject> getRows( StatefulContext context);
}
