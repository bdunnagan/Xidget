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
   * Insert the selected element at the specified index in the selection.
   * @param index The index.
   * @param element The element.
   */
  public void insertSelected( int index, IModelObject element);
  
  /**
   * Remove the selected element from the selection.
   * @param index The index of the element to remove.
   * @param element The element to be removed.
   */
  public void removeSelected( int index, IModelObject element);
  
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
