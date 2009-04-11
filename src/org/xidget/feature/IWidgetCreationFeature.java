/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xmodel.IModelObject;

/**
 * An interface for managing the widget hierarchy. This method supports dynamic
 * application building by allowing the <code>createWidget</code> method to be
 * called more than once.
 */
public interface IWidgetCreationFeature
{
  /**
   * Create a widget with the specified label and configuration element.
   * @param label The optional label.
   * @param element The configuration element.
   */
  public void createWidget( String label, IModelObject element);
  
  /**
   * Destroy the widget(s).
   */
  public void destroyWidget();
}
