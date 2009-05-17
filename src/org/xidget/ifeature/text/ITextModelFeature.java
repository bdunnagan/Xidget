/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.text;

import org.xidget.ifeature.ISourceFeature;
import org.xidget.text.ITextValidator;
import org.xmodel.xpath.expression.IExpression;

/**
 * The interface for setting the text of a text channel. The idea of a text widget
 * is generalized to include a text region. Text regions are defined by a string
 * that describes the channel. The following strings are reserved:
 * <ul>
 * <li><i>default</i> - The default text channel.
 * <li><i>selected</i> - The selected text channel.
 * </ul>
 */
public interface ITextModelFeature extends ISourceFeature
{
  /**
   * Set the text of the source node associated with the specified channel.
   * @param channel The channel.
   * @param text The text.
   */
  public void setText( String channel, String text);
  
  /**
   * Set the expression used to transform the text before it goes into the model.
   * Note that it is the responsibility of the implementation to use this transform
   * if it is provided.
   * @param channel The channel.
   * @param transform The transform.
   */
  public void setTransform( String channel, IExpression transform);
  
  /**
   * Set the object used to validate text when it is committed to the model. The
   * validator does not prevent text from being committed, it simply reports 
   * validation errors using the IErrorAdapter.
   * @param channel The channel.
   * @param validator The validator.
   */
  public void setValidator( String channel, ITextValidator validator);
}
