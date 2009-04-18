/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.ifeature;

import org.xmodel.xpath.expression.IExpression;

/**
 * The interface for setting the text of a text widget. The idea of a text widget
 * is generalized to include a text region. Text regions are defined by a string
 * that describes the channel. The following strings are reserved:
 * <ul>
 * <li><i>default</i> - The default text channel.
 * <li><i>selected</i> - The selected text channel.
 * </ul>
 */
public interface ITextWidgetFeature
{
  /**
   * Set whether the text is editable.
   * @param editable True if editable.
   */
  public void setEditable( boolean editable);
  
  /**
   * Set the text of the widget associated with the specified channel.
   * @param channel The channel.
   * @param text The text.
   */
  public void setText( String channel, String text);
  
  /**
   * Set the expression used to transform the text before it goes into the widget.
   * Note that it is the responsibility of the implementation to use this transform
   * if it is provided.
   * @param channel The channel.
   * @param transform The transform.
   */
  public void setTransform( String channel, IExpression transform);
}
