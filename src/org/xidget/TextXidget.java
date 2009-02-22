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
 * <i>text</i> tag.  Subclasses should usually export an ITextAdapter and an
 * IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#configure(org.xidget.config.TagProcessor, org.xmodel.IModelObject)
   */
  protected boolean configure( TagProcessor processor, IModelObject element)
  {
    return true;
  }

  /**
   * Returns true if the specifid text is valid for this xidget.
   * @param text The text.
   * @return Returns true if the specifid text is valid for this xidget.
   */
  protected boolean validate( String text)
  {
    return true;
  }
  
  /**
   * Commit the specified text to the xidget datamodel.
   * @param text The text.
   */
  protected void commit( String text)
  {
  }
}