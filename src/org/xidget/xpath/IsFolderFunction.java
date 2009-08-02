/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.io.File;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.function.Function;

/**
 * A custom xpath function that returns true if its argument is the path of a folder.
 * The argument must have a result type of string because I'm lazy right now.
 */
public class IsFolderFunction extends Function
{
  public final static String name = "xi:isFolder";
  
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
    return ResultType.BOOLEAN;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateBoolean(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public boolean evaluateBoolean( IContext context) throws ExpressionException
  {
    assertArgs( 1, 1);
    assertType( context, ResultType.STRING);
    
    String value = getArgument( 0).evaluateString( context);
    File file = new File( value);
    return file.isDirectory();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
  {
    if ( oldValue == null) oldValue = "";
    if ( newValue == null) newValue = "";
    
    File oldFile = new File( oldValue);
    File newFile = new File( newValue);
    boolean oldResult = oldFile.isDirectory();
    boolean newResult = newFile.isDirectory();
    if ( !oldResult && newResult) getParent().notifyChange( this, context, true);
    if ( oldResult && !newResult) getParent().notifyChange( this, context, false);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context)
  {
    getParent().notifyChange( this, context);
  }
}
