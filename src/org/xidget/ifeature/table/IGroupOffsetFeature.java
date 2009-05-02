/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.table;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for calculating the row offset for a group.
 */
public interface IGroupOffsetFeature
{
  /**
   * Get group row offset.
   * @param context The parent context.
   * @return Returns the group row offset.
   */
  public int getOffset( StatefulContext context);
}
