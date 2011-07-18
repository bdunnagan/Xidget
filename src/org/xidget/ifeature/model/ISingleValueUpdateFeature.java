/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import org.xmodel.xpath.expression.IExpression;

/**
 * This interface provides methods for updating a widget with a value taken
 * from the model and vice versa. This interface should be used in preference
 * to ISingleValueWidgetFeature or ISingleValueModelFeature since it implements
 * the transformation and validation pipeline, and prevents update loops.
 */
public interface ISingleValueUpdateFeature
{
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
   * Read the value from the model and send it through the transformation and
   * validation pipe to the widget.
   */
  public void updateWidget();
  
  /**
   * Read the value from the widget and send it through the transformation and
   * validation pipe to the model.
   */
  public void updateModel();
  
  /**
   * Transform, validate and display the specified value in the widget.
   * @param value The value taken from the model.
   */
  public void display( Object value);
  
  /**
   * Tranform, validate and commit the specified value in the model.
   * @param value The value taken from the widget.
   */
  public void commit( Object value);
  
  /**
   * Transform and validate the specified value from a model value to a display value.
   * @param value The value.
   * @return Returns null or the transformed value.
   */
  public Object toDisplay( Object value);
  
  /**
   * Transform and validate the specified value from a display value to a model value.
   * @param value The value.
   * @return Returns null or the transformed value.
   */
  public Object toModel( Object value);
}
