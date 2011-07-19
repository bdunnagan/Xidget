/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeWidgetFeature;

/**
 * Xidget utilities.
 */
public class XidgetUtil
{
  /**
   * Find the root of a tree xidget hierarchy. If the xidget is not a tree then return the argument.
   * @param xidget A tree or sub-tree xidget.
   * @return Returns the argument or the root of the tree xidget hierarchy.
   */
  public static IXidget findTreeRoot( IXidget xidget)
  {
    IXidget parent = xidget.getParent();
    while( parent != null)
    {
      ITreeWidgetFeature feature = parent.getFeature( ITreeWidgetFeature.class);
      if ( feature == null) return xidget;
      xidget = parent;
      parent = parent.getParent();
    }
    return xidget;
  }
}
