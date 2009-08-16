/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
