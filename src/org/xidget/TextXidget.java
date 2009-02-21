/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag.
 */
public abstract class TextXidget extends AbstractXidget
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#configure(org.xidget.config.TagProcessor, org.xidget.FormXidget, org.xmodel.IModelObject)
   */
  protected boolean configure( TagProcessor processor, FormXidget parent, IModelObject element)
  {
    return true;
  }
}
