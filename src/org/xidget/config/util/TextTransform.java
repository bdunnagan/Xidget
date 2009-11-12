/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TextTransform.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
