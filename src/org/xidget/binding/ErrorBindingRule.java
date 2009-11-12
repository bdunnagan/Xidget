/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ErrorBindingRule.java
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
package org.xidget.binding;

import org.xidget.IXidget;
import org.xidget.ifeature.IErrorFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule for explicit error tests on xidgets.
 */
public class ErrorBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IErrorFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#getListener(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    return new Listener( xidget, Xlate.get( element, "structure", false));
  }
  
  private class Listener extends ExpressionListener
  {
    public Listener( IXidget xidget, boolean structure)
    {
      this.xidget = xidget;
      this.structure = structure;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      // BUG: multiple error validators stomp on each other.  Need to take any error message if present.
      IErrorFeature feature = xidget.getFeature( IErrorFeature.class);
      if ( structure) feature.structureError( newValue); else feature.valueError( newValue);
    }
    
    private IXidget xidget;
    private boolean structure;
  }
}
