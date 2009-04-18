/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.Stack;
import org.xidget.IXidget;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
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
    this.xidgets = new Stack<IXidget>();
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
      // get parent xidget (before pushing this xidget on the stack)
      IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
      IXidget xidgetParent = (xidgetFeature != null)? xidgetFeature.getXidget(); null;
      
      // instantiate xidget class and store on tag handler to support parenting
      IXidget xidget = xidgetClass.newInstance();
      xidgets.push( xidget);
      
      // configure new xidget
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
    IXidget xidget = xidgets.pop();
    xidget.endConfig( processor, element);

    // emit root object
    if ( parent == null) processor.addRoot( xidget);
  }

  /**
   * Returns the last xidget created by this handler. 
   * @return Returns the last xidget created by this handler.
   */
  public IXidget getLastXidget()
  {
    return xidgets.peek();
  }
  
  private Class<? extends IXidget> xidgetClass;
  private Stack<IXidget> xidgets;
}
