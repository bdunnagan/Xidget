/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.IXidget;
import org.xidget.feature.IBindFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IXidgetBinding which binds the children of a xidget.
 */
public class ChildrenBinding implements IXidgetBinding
{
  public ChildrenBinding( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void bind( StatefulContext context)
  {
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      if ( bindFeature != null) bindFeature.bind( context);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
   */
  public void unbind( StatefulContext context)
  {
    for( IXidget child: xidget.getChildren())
    {
      IBindFeature bindFeature = child.getFeature( IBindFeature.class);
      if ( bindFeature != null) bindFeature.unbind( context);
    }
  }
  
  private IXidget xidget;
}

