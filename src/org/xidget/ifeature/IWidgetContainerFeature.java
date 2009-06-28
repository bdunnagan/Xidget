/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xidget.IXidget;

/**
 * An interface for layout and adding and removing widgets from the container represented by the
 * xidget on which this feature is defined. This interface provides an abstraction for widget
 * containment since widget platforms are usually not smart enough to provide a single means of
 * association. For example in Swing, the JTabbedPane uses a different method to define the tabs of
 * the pane.
 */
public interface IWidgetContainerFeature
{
  /**
   * Add the widget of the specified child to the container.
   * @param child The child.
   */
  public void addWidget( IXidget child);
  
  /**
   * Remove the widget of the specified child to the container.
   * @param child The child.
   */
  public void removeWidget( IXidget child);
  
  /**
   * Tell the container to layout its children again. The container will automatically layout
   * its children when it is first made visible.  This method only needs to be called if the
   * layout changes after the container is visible.
   */
  public void relayout();
  
  /**
   * Returns the internal margins of the container (left, top, right, bottom).
   * @return Returns the internal margins of the container.
   */
  public int[] getInsets();
}
