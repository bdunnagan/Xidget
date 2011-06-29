/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import org.xmodel.xaction.ScriptAction;
import org.xmodel.xpath.expression.IExpression;

/**
 * This interface provides methods for updating a widget with a value taken
 * from the model and vice versa. This interface should be used in preference
 * to ISingleValueWidgetFeature or ISingleValueModelFeature since it implements
 * the transformation and validation pipeline, and prevents update loops.
 */
public interface ISelectionUpdateFeature
{
  /**
   * Transform, validate and display the specified value in the widget. The origin
   * parameter allows the originater of the update to pass through the pipeline to
   * prevent update loops.
   * @param origin The object that originated the update.
   * @param value The value taken from the model.
   */
  public void display( Object origin, Object value);
  
  /**
   * Tranform, validate and commit the specified value to the model. The origin
   * parameter allows the originater of the update to pass through the pipeline to
   * prevent update loops.
   * @param origin The object that originated the update.
   * @param value The value taken from the widget.
   */
  public void commit( Object origin, Object value);
  
  /**
   * The expression used to transform a model value.
   * @param expression The expression.
   */
  public void setDisplayTransform( IExpression expression);
  
  /**
   * The expression used to transform a widget value.
   * @param expression The expression.
   */
  public void setCommitTransform( IExpression expression);
  
  /**
   * Set the script used to transform, validate and/or veto a model value.
   * @param script The script.
   */
  public void setDisplayScript( ScriptAction script);
  
  /**
   * Set the script used to transform, validate and/or veto a widget value.
   * @param script The script.
   */
  public void setCommitScript( ScriptAction script);
}
