/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tree;

import org.xidget.IXidget;
import org.xidget.feature.model.SelectionModelFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.util.XidgetUtil;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A customization of SelectionModelFeature that gets the context from the xidget that created the widget
 * since the nested tree xidgets are bound to row contexts.  The row context is not used for script evaluation
 * because the scripts are intended to create side-effects which may include creating new variables.
 */
public class SubTreeSelectionModelFeature extends SelectionModelFeature
{
  public SubTreeSelectionModelFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionModelFeature#getContext()
   */
  @Override
  protected StatefulContext getContext()
  {
    IBindFeature bindFeature = XidgetUtil.findTreeRoot( xidget).getFeature( IBindFeature.class);
    return bindFeature.getBoundContext();
  }
}
