/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xidget.IXidget;

/**
 * An interface exported by IToolkit implementations that provides a printing facility. The printing facility
 * creates the xidget according to its configuration in a container whose dimensions are that of the page to
 * be printed.  Only the imageable area of the page is considered, so the client need not be concerned with
 * margins.
 */
public interface IPrintFeature
{
  /**
   * Print the specified xidgets.
   * @param xidgets The xidgets to be printed.
   */
  public void print( List<IXidget> xidgets);
}
