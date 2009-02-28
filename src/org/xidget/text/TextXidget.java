/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.util.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag.  Subclasses should usually export an ITextAdapter and an
 * IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    setParent( parent);
   
    // create widget
    Size size = new Size( Xlate.childGet( element, "size", ""), 1, 0);
    createWidget( size);
    
    return true;
  }

  /**
   * Create the widget with the specified configuration parameters. If the 
   * y-coordinate is 0 then the column size is unbounded. The row size will 
   * always be bounded.
   * @param size The size of the widget in characters.
   */
  protected abstract void createWidget( Size size) throws TagException;
  
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