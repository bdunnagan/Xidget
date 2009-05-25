/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xmodel.IModelObject;

/**
 * (context-specific)
 * An interface for managing the selection model. The selection is defined by a subset
 * of the children of a single parent element.  The subset is defined by a filter 
 * expression.
 */
public interface ISelectionModelFeature
{
  /**
   * Set the selection corresponding to the specified nodes.
   * @param nodes The selected nodes.
   */
  public void setSelection( List<IModelObject> nodes);
  
  /**
   * Returns the currently selected nodes. Note that the platform-specific widget
   * is required so that the filter expression can be evaluated with the correct
   * parent context.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection();
  
  /**
   * Returns a unique identity for the node. 
   * @param node The node.
   * @return Returns a unique identity for the node.
   */
  public Object getIdentity( IModelObject node);
}
