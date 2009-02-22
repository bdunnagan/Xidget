/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * A base implementation of ITagHandler for xidgets that handles xidget parenting.
 */
public class XidgetTagHandler implements ITagHandler
{
  public XidgetTagHandler( String tag, IXidget xidget)
  {
    this.tag = tag;
    this.xidget = xidget;
  }
  
  /**
   * Returns the xidget associated with this tag handler.
   * @return Returns the xidget associated with this tag handler.
   */
  public IXidget getXidget()
  {
    return xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#getTag()
   */
  public String getTag()
  {
    return tag;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#process(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean process( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( parent instanceof XidgetTagHandler) xidget.setParent( ((XidgetTagHandler)parent).getXidget());
    return true;
  }

  private String tag;
  private IXidget xidget;
}
