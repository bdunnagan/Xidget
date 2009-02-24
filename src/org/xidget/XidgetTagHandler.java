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
  /**
   * Create a xidget tag handler which instantiates xidgets of the specified class.
   * @param xidgetClass The implementation class of IXidget.
   */
  public XidgetTagHandler( Class<? extends IXidget> xidgetClass)
  {
    this.xidgetClass = xidgetClass;
  }
    
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    try
    {
      // instantiate xidget class and store on tag handler to support parenting
      xidget = xidgetClass.newInstance();
      
      // configure new xidget
      IXidget xidgetParent = (parent instanceof XidgetTagHandler)? ((XidgetTagHandler)parent).getLastXidget(): null;
      return xidget.startConfig( processor, xidgetParent, element);
    }
    catch( InstantiationException e)
    {
      throw new TagException( "Unable to create xidget of class: "+xidgetClass, e);
    }
    catch( IllegalAccessException e)
    {
      throw new TagException( "Access denied for xidget class: "+xidgetClass, e);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    xidget.endConfig( processor, element);
    xidget = null;
  }

  /**
   * Returns the last xidget created by this handler. 
   * @return Returns the last xidget created by this handler.
   */
  protected IXidget getLastXidget()
  {
    return xidget;
  }
  
  private Class<? extends IXidget> xidgetClass;
  private IXidget xidget;
}
