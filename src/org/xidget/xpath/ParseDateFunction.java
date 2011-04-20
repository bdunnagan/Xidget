/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.util.List;

import org.xidget.util.DateUtil;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * XPath function to parse a formatted date string.
 */
public class ParseDateFunction extends Function
{
  public final static String name = "xi:parse-date";
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.IExpression#getName()
   */
  @Override
  public String getName()
  {
    return name;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.IExpression#getType()
   */
  @Override
  public ResultType getType()
  {
    return ResultType.NUMBER;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateNumber(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public double evaluateNumber( IContext context) throws ExpressionException
  {
    assertArgs( 2, 2);
    
    IExpression format = getArgument( 0);
    IExpression value = getArgument( 1);
    
    DateUtil util = new DateUtil();
    return util.parse( format.evaluateString( context), value.evaluateString( context));
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyAdd(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyRemove(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
  {
    IExpression formatExpr = getArgument( 0);
    IExpression valueExpr = getArgument( 1);
    if ( expression == formatExpr)
    {
      DateUtil util = new DateUtil();
      String value = valueExpr.evaluateString( context);
      
      long oldResult = util.parse( oldValue, value);
      long newResult = util.parse( newValue, value);
      
      getParent().notifyChange( this, context, (double)newResult, (double)oldResult);
    }
    else
    {
      DateUtil util = new DateUtil();
      String format = formatExpr.evaluateString( context);
      
      long oldResult = util.parse( format, oldValue);
      long newResult = util.parse( format, newValue);
      
      getParent().notifyChange( this, context, (double)newResult, (double)oldResult);
    }
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyValue(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    String oldString = (oldValue != null)? oldValue.toString(): "";
    String newString = (newValue != null)? newValue.toString(): "";
    notifyChange( expression, contexts[ 0], newString, oldString);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#requiresValueNotification(org.xmodel.xpath.expression.IExpression)
   */
  @Override
  public boolean requiresValueNotification( IExpression argument)
  {
    return true;
  }
}
