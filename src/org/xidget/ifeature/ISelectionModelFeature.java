/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for managing the selection model. The selection is defined by a subset
 * of the children of a single parent element.  The subset is defined by a filter 
 * expression.
 */
public interface ISelectionModelFeature
{
  /**
   * Set the parent of the selection.
   * @param context The context in which the parent expression was evaluated.
   * @param element The parent element.
   */
  public void setParent( StatefulContext context, IModelObject element);
  
  /**
   * Set the filter expression.
   * @param filter The filter expression.
   */
  public void setFilter( IExpression filter);
  
  /**
   * Set the selection corresponding to the specified nodes.
   * @param widget The platform-specific widget object that changed the selection.
   * @param nodes The selected nodes.
   */
  public void setSelection( Object widget, List<IModelObject> nodes);
  
  /**
   * Returns the currently selected nodes. Note that the platform-specific widget
   * is required so that the filter expression can be evaluated with the correct
   * parent context.
   * @param widget The platform-specific widget object that changed the selection.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection( Object widget);
}
