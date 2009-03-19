/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.feature;

/**
 * An interface for setting an icon. This feature is only appropriate 
 * for xidgets which only display a single icon. It is used by the 
 * IconBindingRule. For text xidgets, this is the icon that is 
 * displayed with the label.
 */
public interface IIconFeature
{
  /**
   * Set the xidget icon.
   * @param The xidget icon.
   */
  public void setIcon( Object icon);
}
