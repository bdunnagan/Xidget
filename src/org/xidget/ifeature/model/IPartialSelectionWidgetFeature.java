/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

import org.xidget.IXidget;

/**
 * Since tables may consist of multiple table declarations, the parent table xidget must provide an interface
 * for localizing selection events to the rows associated with a particular nested table declaration.  This
 * interface serves that purpose by providing the same methods as ISelectionWidgetFeature with the addition
 * of an IXidget parameter that localizes the update.
 */
public interface IPartialSelectionWidgetFeature
{
  /**
   * Add the specified object to the selection.
   * @param origin A nested table xidget.
   * @param object The object.
   */
  public void select( IXidget origin, Object object);
  
  /**
   * Remove the specified object for the selection.
   * @param origin A nested table xidget.
   * @param object The object.
   */
  public void deselect( IXidget origin, Object object);
  
  /**
   * Set the list of selected objects.
   * @param origin A nested table xidget.
   * @param list The list of selected objects.
   */
  public void setSelection( IXidget origin, List<? extends Object> list);
  
  /**
   * Returns the list of selected objects belonging to the specified xidget.
   * @param origin A nested table xidget.
   * @return Returns the list of selected objects.
   */
  public List<? extends Object> getSelection( IXidget xidget);
}
