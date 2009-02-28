/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

/**
 * An interface for a text widget used by TextXidget.
 */
public interface IWidgetTextChannel
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
  
  /**
   * Add a text widget listener.
   * @param listener The listener.
   */
  public void addListener( IListener listener);
  
  /**
   * Remove a text widget listener.
   * @param listener The listener.
   */
  public void removeListener( IListener listener);
  
  /**
   * Interface for text widget events.
   */
  public interface IListener
  {
    /**
     * Called when the text of a widget changes.
     * @param text The new text.
     */
    public void onTextChanged( String text);
  }
}
