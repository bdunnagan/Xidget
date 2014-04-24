/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.text.ParseException;
import java.util.List;
import java.util.TimeZone;

import org.xidget.util.DateFormat;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * XPath function to parse a formatted date string. This function takes two arguments: a format string
 * as defined by the org.xidget.util.DateUtil class, and the date string to be parsed. If the date 
 * cannot be parsed then 0 is returned.
 */
public class DateParseFunction extends Function
{
  public final static String name = "xi:date-parse";
  
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
    assertArgs( 2, 3);
    
    IExpression formatExpr = getArgument( 0);
    IExpression valueExpr = getArgument( 1);
    IExpression timeZoneExpr = getArgument( 2);
    
    try
    {
      String format = formatExpr.evaluateString( context);
      String value = valueExpr.evaluateString( context);
      String timeZoneAbbr = (timeZoneExpr != null)? timeZoneExpr.evaluateString( context): null;
      TimeZone timeZone = (timeZoneAbbr != null)? TimeZone.getTimeZone( timeZoneAbbr): null;
      DateFormat util = new DateFormat();
      return util.parse( format, value, timeZone);
    }
    catch( Exception e)
    {
      throw new ExpressionException( this, "Unable to parse date.", e);
    }
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
      DateFormat util = new DateFormat();
      String value = valueExpr.evaluateString( context);
      
      try
      {
        long oldResult = util.parse( oldValue, value);
        long newResult = util.parse( newValue, value);
        getParent().notifyChange( this, context, (double)newResult, (double)oldResult);
      }
      catch( ParseException e)
      {
      }
    }
    else
    {
      DateFormat util = new DateFormat();
      String format = formatExpr.evaluateString( context);
      
      try
      {
        long oldResult = util.parse( format, oldValue);
        long newResult = util.parse( format, newValue);
        getParent().notifyChange( this, context, (double)newResult, (double)oldResult);
      }
      catch( ParseException e)
      {
      }
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
