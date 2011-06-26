/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xpath;

import java.util.ArrayList;
import java.util.List;

import org.xidget.Creator;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.function.Function;

/**
 * XPath function that returns the names of the available fonts.
 */
public class FontsFunction extends Function
{
  public final static String name = "xi:fonts";
  
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
    return ResultType.NODES;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateNodes(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public List<IModelObject> evaluateNodes( IContext context) throws ExpressionException
  {
    assertArgs( 0, 0);

    if ( result != null) return result;
    
    List<String> fonts = Creator.getInstance().getToolkit().getFonts();
    result = new ArrayList<IModelObject>( fonts.size());
    for( String font: fonts)
    {
      ModelObject fontNode = new ModelObject( "font");
      fontNode.setValue( font);
      result.add( fontNode);
    }
    
    return result;
  }
  
  private List<IModelObject> result;
}
