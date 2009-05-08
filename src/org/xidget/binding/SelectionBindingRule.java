/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the widget selection.
 */
public class SelectionBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    // TODO Auto-generated method stub
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.IBindingRule#getListener(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public IExpressionListener getListener( IXidget xidget, IModelObject element)
  {
    // TODO Auto-generated method stub
    return null;
  }
}
