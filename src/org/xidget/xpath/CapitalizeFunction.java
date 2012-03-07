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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;
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
  
  public CapitalizeFunction()
  {
    map = new WeakHashMap<IModelObject, IModelObject>();
  }
  
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
    assertArgs( 1, 1);
    return getArgument( 0).getType();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#getType(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public ResultType getType( IContext context)
  {
    assertArgs( 1, 1);
    return getArgument( 0).getType( context);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateNodes(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public List<IModelObject> evaluateNodes( IContext context) throws ExpressionException
  {
    assertArgs( 1, 1);
    
    List<IModelObject> inNodes = getArgument( 0).evaluateNodes( context);
    return capitalize( inNodes);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateString(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public String evaluateString( IContext context) throws ExpressionException
  {
    assertArgs( 1, 1);

    String input = getArgument( 0).evaluateString( context);
    if ( input.length() == 0) return input;
    
    return capitalize( input);
  }
  
  /**
   * Capitalize the value of each node.
   * @param nodes The nodes.
   * @return Returns associated nodes with capitalized values.
   */
  private List<IModelObject> capitalize( List<IModelObject> nodes)
  {
    List<IModelObject> outNodes = new ArrayList<IModelObject>( nodes.size());
    for( IModelObject inNode: nodes)
    {
      IModelObject outNode = map.get( inNode);
      if ( outNode == null)
      {
        outNode = new ModelObject( inNode.getType());
        outNode.setValue( capitalize( Xlate.get( inNode, "")));
        map.put( outNode, inNode);
      }
      outNodes.add( outNode);
    }
    return outNodes;
  }

  /**
   * Capitalize the specified string.
   * @param s The string.
   * @return Returns the capitalized string.
   */
  private static String capitalize( String s)
  {
    if ( s == null) return null;
    
    StringBuilder sb = new StringBuilder();
    sb.append( Character.toUpperCase( s.charAt( 0)));
    sb.append( s, 1, s.length());
    return sb.toString();
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyAdd(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes = capitalize( nodes);
    getParent().notifyAdd( this, context, nodes);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes = capitalize( nodes);
    getParent().notifyRemove( this, context, nodes);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, boolean)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, boolean newValue)
  {
    getParent().notifyChange( this, context, newValue);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, double, double)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
  {
    getParent().notifyChange( this, context, newValue, oldValue);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyChange(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
   */
  @Override
  public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
  {
    getParent().notifyChange( this, context, capitalize( newValue), capitalize( oldValue));
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#notifyValue(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    String newString = (newValue != null)? capitalize( newValue.toString()): null;
    String oldString = (oldValue != null)? capitalize( oldValue.toString()): null;
    
    object = map.get( object);
    object.setValue( newString);
    
    getParent().notifyValue( this, contexts, object, newString, oldString);
  }
  
  private Map<IModelObject, IModelObject> map;
}
