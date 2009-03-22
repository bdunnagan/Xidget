/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.IXidget;
import org.xmodel.IModelObject;

/**
 * An interface for managing the widget hierarchy. This method supports dynamic
 * application building by allowing the <code>createWidget</code> method to be
 * called more than once.
 */
public interface IWidgetHierarchyFeature
{
  /**
   * Create a widget with the specified label and configuration element.
   * This method may be called more than once.  If the widget has already been 
   * created, it should be discarded and a new widget created.  The widget hierarchy
   * should be refreshed to accomodate the new layout.
   * @param xidget The xidget for which the widget is being created.
   * @param label The optional label.
   * @param element The configuration element.
   */
  public void createWidget( IXidget xidget, String label, IModelObject element);
}
