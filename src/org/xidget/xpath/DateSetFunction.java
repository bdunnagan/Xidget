/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.util.List;
import org.xidget.util.DateFormat;
import org.xidget.util.DateFormat.Field;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * A custom XPath function that sets a date field.  The function takes three arguments:
 *   1. The absolute time in milliseconds.
 *   2. The field to be modified (see DateFormat for names of fields).
 *   3. The units to assign to the field.
 * 
 * The field argument is not bound.
 */
public class DateSetFunction extends Function
{
  public final static String name = "xi:date-set";
  
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
    assertArgs( 3, 3);
    
    long time = (long)getArgument( 0).evaluateNumber( context);
    int units = (int)getArgument( 2).evaluateNumber( context);
    
    if ( getArgument( 1).getType( context) == ResultType.NODES)
    {
      for( IModelObject node: getArgument( 1).evaluateNodes( context))
      {
        String fieldName = Xlate.get( node, "");
        Field field = DateFormat.parseFieldName( fieldName);
        if ( field == null) throw new ExpressionException( this, "Illegal field value: "+fieldName);
        time = DateFormat.fieldSet( time, field, units);
      }
    }
    else
    {
      String[] fieldNames = getArgument( 1).evaluateString( context).split(  "\\s*,\\s*");
      for( String fieldName: fieldNames)
      {
        Field field = DateFormat.parseFieldName( fieldName);
        if ( field == null) throw new ExpressionException( this, "Illegal field value: "+fieldName);
        time = DateFormat.fieldSet( time, field, units);
      }
    }
    
    return time;
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
