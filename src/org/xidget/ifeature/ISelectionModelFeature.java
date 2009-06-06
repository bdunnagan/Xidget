/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for managing the selection model.
 */
public interface ISelectionModelFeature
{
  /**
   * Set the parent of the selection.
   * @param context The parent context.
   * @param element The selection parent element.
   */
  public void setParent( StatefulContext context, IModelObject element);
  
  /**
   * Insert the selected element into the selection model.
   * @param context The parent context.
   * @param index The index of the insertion.
   * @param element The element.
   */
  public void insertSelected( StatefulContext context, int index, IModelObject element);
  
  /**
   * Remove the selected element from the selection model.
   * @param context The parent context.
   * @param index The index of the removals.
   * @param element The element.
   */
  public void removeSelected( StatefulContext context, int index, IModelObject element);
  
  /**
   * Set the selection corresponding to the specified nodes.
   * @param context The parent context.
   * @param nodes The selected nodes.
   */
  public void setSelection( StatefulContext context, List<IModelObject> nodes);
  
  /**
   * Returns the currently selected nodes.
   * @param context The parent context.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection( StatefulContext context);
  
  /**
   * Returns a unique identity for the node. 
   * @param node The node.
   * @return Returns a unique identity for the node.
   */
  public Object getIdentity( IModelObject node);
}
