/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for handling the automatic creation and deletion of optional nodes as defined by a schema.
 */
public interface IOptionalNodeFeature
{
  /**
   * Set the source expression for the specified channel.
   * @param channel The channel.
   * @param expression The expression.
   */
  public void setSourceExpression( String channel, IExpression expression);

  /**
   * Create optional nodes based on the source expression.
   * @param channel The channel.
   * @param context The context.
   */
  public void createOptionalNodes( String channel, StatefulContext context);
  
  /**
   * Delete optional nodes based on the source expression.
   * @param channel The channel.
   * @param context The context.
   */
  public void deleteOptionalNodes( String channel, StatefulContext context);
}
