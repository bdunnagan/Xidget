/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.widget;

/**
 * An interface for a text widget used by TextXidget.
 */
public interface ITextWidgetAdapter extends IWidgetAdapter
{
  /**
   * Set whether the text in the widget is editable.
   * @param editable True if editable.
   */
  public void setEditable( boolean editable);
  
  /**
   * Display the specified text in the widget.
   * @param text The text.
   */
  public void setText( String text);
}
