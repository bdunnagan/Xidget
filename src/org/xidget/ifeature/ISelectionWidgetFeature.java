/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xmodel.IModelObject;

/**
 * An interface for getting and setting the selection of a widget.
 */
public interface ISelectionWidgetFeature
{
  /**
   * Set the selection corresponding to the specified nodes.
   * @param nodes The selected nodes.
   */
  public void setSelection( List<IModelObject> nodes);
  
  /**
   * Returns the currently selected nodes.
   * @return Returns the currently selected nodes.
   */
  public List<IModelObject> getSelection();
}
