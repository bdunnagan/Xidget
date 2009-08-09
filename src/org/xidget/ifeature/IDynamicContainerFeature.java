/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for containers that support dynamic addition and removal of children, like a tab form.
 * This feature is not part of the dynamic rebuilding capability of the framework, which is a lower-level
 * capability.  This feature might be implemented using that capability, however.
 */
public interface IDynamicContainerFeature
{
  /**
   * Update the list of children for this container. The implementation is responsible for insuring
   * that there are zero or one child for each node and that children are inserted into the container
   * in the correct order.
   * @param context The parent context. 
   * @param nodes The nodes.
   */
  public void setChildren( StatefulContext context, List<IModelObject> nodes);
}
