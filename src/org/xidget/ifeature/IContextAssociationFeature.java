/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for associating features to a specific context.  This interface provides
 * the means for a xidget to export a context-specific feature set.  In other words, each
 * context is associated with its own instance of each feature class.
 */
public interface IContextAssociationFeature
{
  /**
   * Create features for the specified context.
   * @param context The context.
   */
  public void createFeatures( StatefulContext context);
  
  /**
   * Delete features for the specified context.
   * @param context The context.
   */
  public void deleteFeatures( StatefulContext context);
  
  /**
   * Returns null or an instance of the context-specific feature.
   * @param context The context.
   * @param clss The interface class.
   * @return Returns null or an instance of the context-specific feature.
   */
  public <T> T getFeature( StatefulContext context, Class<T> clss);  
}
