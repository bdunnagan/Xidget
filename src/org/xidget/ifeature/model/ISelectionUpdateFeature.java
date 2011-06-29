/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.model;

import java.util.List;

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
   * Transform, validate and display the specified selection in the widget. The origin
   * parameter allows the originater of the update to pass through the pipeline to
   * prevent update loops.
   * @param origin The object that originated the update.
   * @param list The list of selected values.
   */
  public void setSelectionInWidget( Object origin, List<? extends Object> list);
  
  /**
   * Transform, validate and insert the specified value into the selection in the widget.
   * The origin parameter allows the originater of the update to pass through the pipeline 
   * to prevent update loops.
   * @param origin The object that originated the update.
   * @param index The index where the value is to be inserted.
   * @param value The value.
   */
  public void insertSelectedInWidget( Object origin, int index, Object value);
  
  /**
   * Remove the specified value from the selection in the widget.
   * @param origin The object that originated the update.
   * @param index The index of the value to be removed.
   */
  public void removeSelectedInWidget( Object origin, int index);
  
  /**
   * Transform, validate and display the specified selection in the model. The origin
   * parameter allows the originater of the update to pass through the pipeline to
   * prevent update loops.
   * @param origin The object that originated the update.
   * @param list The list of selected values.
   */
  public void setSelectionInModel( Object origin, List<? extends Object> list);
  
  /**
   * Transform, validate and insert the specified value into the selection in the model.
   * The origin parameter allows the originater of the update to pass through the pipeline 
   * to prevent update loops.
   * @param origin The object that originated the update.
   * @param index The index where the value is to be inserted.
   * @param value The value.
   */
  public void insertSelectedInModel( Object origin, int index, Object value);
  
  /**
   * Remove the specified value from the selection in the model.
   * @param origin The object that originated the update.
   * @param index The index of the value to be removed.
   */
  public void removeSelectedInModel( Object origin, int index);
  
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
