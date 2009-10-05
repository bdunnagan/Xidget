/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.util;

import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A transform represented by an expression. The input text to the transform 
 * is placed in the $v variable. The context object is a dummy.
 */
public class TextTransform
{
  public TextTransform( IExpression expression)
  {
    this.expression = expression;
  }
  
  /**
   * Transform the specified text.
   * @param context The parent context.
   * @param text The text.
   * @return Returns the transformed text.
   */
  public String transform( StatefulContext context, String text)
  {
    StatefulContext transformContext = new StatefulContext( context);
    transformContext.set( "v", text);
    return expression.evaluateString( transformContext);
  }
  
  private IExpression expression;
}
