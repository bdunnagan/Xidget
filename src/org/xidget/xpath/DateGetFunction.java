/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.util.List;
import org.xidget.util.DateFormat;
import org.xidget.util.DateFormat.Field;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * A custom XPath function that gets a date field.  The function takes three arguments:
 *   1. The absolute time in milliseconds.
 *   2. The field to be modified (see DateFormat for names of fields).
 * 
 * The field argument is not bound.
 */
public class DateGetFunction extends Function
{
  public final static String name = "xi:date-get";
  
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
    
    long time = (long)getArgument( 0).evaluateNumber( context);
    String fieldName = getArgument( 1).evaluateString( context);
    Field field = DateFormat.parseFieldName( fieldName);
    if ( field == null) throw new ExpressionException( this, "Illegal field value: "+fieldName);
    return DateFormat.fieldGet( time, field);
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
    getParent().notifyChange( this, context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyValue(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    getParent().notifyChange( this, contexts[ 0]);
  }
}
