/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tab;

/**
 * An interface for defining the title and/or icon for a tab in a tab group.
 */
public interface ITabFeature
{
  /**
   * Set the title of the tab.
   * @param title The title.
   */
  public void setTitle( String title);
  
  /**
   * Set the icon of the tab.
   * @param image The icon image.
   */
  public void setIcon( Object image);
}
