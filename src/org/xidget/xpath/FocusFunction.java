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

import java.util.Collections;
import java.util.List;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.IFocusFeature;
import org.xidget.ifeature.IFocusFeature.IFocusListener;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.function.Function;

/**
 * A custom XPath that returns the configuration of the xidget that currently has focus.
 */
public class FocusFunction extends Function
{
  public final static String name = "xi:focus";
  
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
    return ResultType.NODES;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#evaluateNodes(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public List<IModelObject> evaluateNodes( IContext context) throws ExpressionException
  {
    assertArgs( 0, 0);

    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    if ( focusFeature != null)
    {
      IXidget xidget = focusFeature.getFocus();
      if ( xidget != null) return Collections.singletonList( xidget.getConfig());
    }
    
    return Collections.emptyList();
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#bind(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public void bind( IContext context)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    if ( focusFeature != null)
    {
      focusFeature.addFocusListener( new FocusListener( context));
    }
  }

  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.Expression#unbind(org.xmodel.xpath.expression.IContext)
   */
  @Override
  public void unbind( IContext context)
  {
    IFocusFeature focusFeature = Creator.getToolkit().getFeature( IFocusFeature.class);
    if ( focusFeature != null)
    {
      focusFeature.removeFocusListener( new FocusListener( context));
    }
  }
  
  private class FocusListener implements IFocusListener
  {
    public FocusListener( IContext context)
    {
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.xidget.ifeature.IFocusFeature.IFocusListener#notifyFocus(org.xidget.IXidget, org.xidget.IXidget)
     */
    @Override
    public void notifyFocus( IXidget newFocus, IXidget oldFocus)
    {
      getParent().notifyRemove( FocusFunction.this, context, Collections.singletonList( oldFocus.getConfig()));
      getParent().notifyAdd( FocusFunction.this, context, Collections.singletonList( newFocus.getConfig()));
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( object instanceof FocusListener)
      {
        FocusListener listener = (FocusListener)object;
        return listener.context.equals( context);
      }
      return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      return context.hashCode();
    }

    private IContext context;
  };
}
