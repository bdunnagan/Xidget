/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

/**
 * This interface defines the update semantics for the selected items of a widget.
 */
public interface ISelectionUpdateFeature
{
  public enum Mode { ref, fk1, fk2};
  
  /**
   * Set the selection mode. The selection mode determines how a selected object is transformed before
   * it is added to the selection.  The selection transforms are the same ones available through the
   * <i>assign</i>, <i>add</i> and <i>copy</i> actions.
   * @param mode The selection mode.
   */
  public void setMode( Mode mode);
  
  /**
   * Update the widget selection based on the selection in the model. This method retrieves
   * the entire selection from the widget and the model and determines what objects need
   * to be inserted and removed to synchronize them.
   * @param context The context.
   */
  public void updateWidget();
  
  /**
   * Update the model selection based on the selection in the widget. This method retrieves
   * the entire selection from the widget and the model and determines what objects need
   * to be inserted and removed to synchronize them.
   */
  public void updateModel();
  
  /**
   * Add the specified objects to the display selection.
   * @param objects The objects.
   */
  public void displaySelect( List<? extends Object> objects);
  
  /**
   * Remove the specified objects from the display selection.
   * @param objects The objects.
   */
  public void displayDeselect( List<? extends Object> objects);
  
  /**
   * Add the specified objects to the model selection.
   * @param objects The objects.
   */
  public void modelSelect( List<? extends Object> objects);
  
  /**
   * Remove the specified objects from the model selection.
   * @param objects The objects.
   */
  public void modelDeselect( List<? extends Object> objects);
  
  /**
   * Transform the specified model selection to a display selection according to the mode.
   * @param selected The model selection.
   * @return Returns the display selection.
   */
  public Object toDisplay( Object selected);
  
  /**
   * Transform the specified display selection to a model selection according to the mode.
   * @param selected The display selection.
   * @return Returns the model selection.
   */
  public Object toModel( Object selected);
}
