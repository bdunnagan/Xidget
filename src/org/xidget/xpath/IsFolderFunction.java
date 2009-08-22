/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.io.File;
import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
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
  public final static String name = "xi:is-folder";
  
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
    
    IExpression arg0 = getArgument( 0);
    ResultType type = arg0.getType( context);
    if ( type == ResultType.STRING)
    {
      String value = getArgument( 0).evaluateString( context);
      File file = new File( value);
      return file.isDirectory();
    }
    else if ( type == ResultType.NODES)
    {
      List<IModelObject> nodes = arg0.evaluateNodes( context);
      for( IModelObject node: nodes)
      {
        String value = Xlate.get( node, "");
        File file = new File( value);
        if ( !file.isDirectory()) return false; 
      }
      return true;
    }
    else
    {
      return false;
    }
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
}
