/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.text;

import org.xidget.text.ITextValidator;
import org.xmodel.IModelObject;
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
public interface ITextModelFeature
{
  /**
   * Set the source node which stores the text. 
   * This method does not need to call <code>setText</code>.
   * @param channel The channel.
   * @param node The source node.
   */
  public void setSource( String channel, IModelObject node);
  
  /**
   * Returns the source node which stores the text.
   * @param channel The channel.
   * @return Returns the source node which stores the text.
   */
  public IModelObject getSource( String channel);
  
  /**
   * Set the text of the widget associated with the specified channel.
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
