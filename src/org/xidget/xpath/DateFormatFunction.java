/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.util.List;
import java.util.TimeZone;

import org.xidget.util.DateFormat;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * XPath function to format a date stored as a number representing milliseconds since January 1, 1971.
 * Note that this implementation differs from the xsl:format-date function because this library does
 * not support XSLT.
 */
public class DateFormatFunction extends Function
{
  public final static String name = "xi:date-format";
  
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
    return ResultType.STRING;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateString(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public String evaluateString( IContext context) throws ExpressionException
  {
    assertArgs( 2, 3);
    
    IExpression format = getArgument( 0);
    IExpression value = getArgument( 1);
    IExpression timeZoneExpr = getArgument( 2);

    long time = (long)value.evaluateNumber( context);
    if ( time == 0) return "";
    
    String timeZoneAbbr = (timeZoneExpr != null)? timeZoneExpr.evaluateString( context): null;
    TimeZone timeZone = (timeZoneAbbr != null)? TimeZone.getTimeZone( timeZoneAbbr): null;
    
    DateFormat util = new DateFormat();
    return util.format( format.evaluateString( context), time, timeZone);
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
   * org.xmodel.xpath.expression.IContext, double, double)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
  {
    IExpression formatExpr = getArgument( 0);
    IExpression valueExpr = getArgument( 1);
    if ( expression == valueExpr)
    {
      DateFormat util = new DateFormat();
      String format = formatExpr.evaluateString( context);
      String oldResult = util.format( format, (long)oldValue);
      String newResult = util.format( format, (long)newValue);
      getParent().notifyChange( this, context, newResult, oldResult);
    }
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
      long value = (long)valueExpr.evaluateNumber( context);
      
      String oldResult = util.format( oldValue, value);
      String newResult = util.format( newValue, value);
      
      getParent().notifyChange( this, context, newResult, oldResult);
    }
    else
    {
      DateFormat util = new DateFormat();
      String format = formatExpr.evaluateString( context);
      
      double oldDouble = parseDouble( oldValue);
      String oldResult = (oldDouble == Double.MIN_VALUE)? "": util.format( format, (long)oldDouble);
      
      double newDouble = parseDouble( newValue);
      String newResult = (newDouble == Double.MIN_VALUE)? "": util.format( format, (long)newDouble);
      
      getParent().notifyChange( this, context, newResult, oldResult);
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

  /**
   * Quietly parse a double from the specified string.
   * @param string The string.
   * @return Returns Double.MIN_VALUE or the parsed string.
   */
  private double parseDouble( String string)
  {
    try
    {
      return Double.parseDouble( string);
    }
    catch( Exception e)
    {
      return Double.MIN_VALUE;
    }
  }
}
