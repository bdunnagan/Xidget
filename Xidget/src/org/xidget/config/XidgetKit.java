/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import org.xidget.config.processor.ITagHandler;

/**
 * A base implementation of IXidgetKit which returns null for all xidgets. This class
 * exists to allow xidget kits to use new releases of the xidget framework without
 * necessarily implementing new xidget types that have been added to the kit.
 */
public class XidgetKit implements IXidgetKit
{
  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getApplicationHandler()
   */
  public ITagHandler getApplicationHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getButtonHandler()
   */
  public ITagHandler getButtonHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getComboHandler()
   */
  public ITagHandler getComboHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getDialogHandler()
   */
  public ITagHandler getDialogHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getFormHandler()
   */
  public ITagHandler getFormHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getSliderHandler()
   */
  public ITagHandler getSliderHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getTableHandler()
   */
  public ITagHandler getTableHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getTextHandler()
   */
  public ITagHandler getTextHandler()
  {
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.IXidgetKit#getTreeHandler()
   */
  public ITagHandler getTreeHandler()
  {
    return null;
  }
}
