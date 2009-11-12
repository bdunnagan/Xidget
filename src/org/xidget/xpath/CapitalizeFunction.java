/*
 * Xidget - XML Widgets based on JAHM
 * 
 * CapitalizeFunction.java
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
package org.xidget.xpath;

import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * A custom XPath function that capitalizes the first letter of its argument.
 */
public class CapitalizeFunction extends Function
{
  public final static String name = "xi:capitalize";
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.IExpression#getName()
   */
  public String getName()
  {
    return name;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.IExpression#getType()
   */
  public ResultType getType()
  {
    return ResultType.STRING;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateString(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public String evaluateString( IContext context) throws ExpressionException
  {
    assertArgs( 1, 1);
    
    String input = getArgument( 0).evaluateString( context);
    StringBuilder sb = new StringBuilder();
    sb.append( Character.toUpperCase( input.charAt( 0)));
    sb.append( input, 1, input.length());
    
    return sb.toString();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyAdd(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, boolean)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, boolean newValue)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, double, double)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyValue(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    getParent().notifyChange( this, contexts[ 0]);
  }
}
