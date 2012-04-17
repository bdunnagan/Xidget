/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

import org.xmodel.IModelObject;

/**
 * An interface to the list of selected objects stored in a model.
 */
public interface ISelectionModelFeature
{
  /**
   * Set the name of a context variable where the selection will be stored.
   * @param name The name of the variable.
   */
  public void setSourceVariable( String name);
  
  /**
   * Set the parent of the selection.
   * @param node The node.
   */
  public void setSourceNode( IModelObject node);
  
  public enum Mode { ref, fk1, fk2};
  
  /**
   * Set the selection mode. The selection mode determines how a selected object is transformed before
   * it is added to the selection.  The selection transforms are the same ones available through the
   * <i>assign</i>, <i>add</i> and <i>copy</i> actions.
   * @param mode The selection mode.
   */
  public void setMode( Mode mode);
  
  /**
   * Add the specified object to the selection.
   * @param object The object.
   */
  public void select( Object object);

  /**
   * Remove the specified object for the selection.
   * @param object The object.
   */
  public void deselect( Object object);
  
  /**
   * Set the list of selected objects.
   * @param list The list of selected objects.
   */
  public void setSelection( List<? extends Object> list);
  
  /**
   * @return Returns the list of selected objects.
   */
  public List<? extends Object> getSelection();
}
