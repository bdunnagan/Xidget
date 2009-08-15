/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.Stack;
import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of ITagHandler for processing xidget declarations. This handler will
 * not process declarations that have a <i>when</i> attribute or element, as these
 * declarations belong to a ConfigurationSwitch, which is lazily constructed.
 */
public class XidgetTagHandler extends AbstractTagHandler implements IXidgetFeature
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
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    try
    {
      // get parent xidget (before pushing this xidget on the stack)
      IXidgetFeature xidgetFeature = (parent != null)? parent.getFeature( IXidgetFeature.class): null;
      IXidget xidgetParent = (xidgetFeature != null)? xidgetFeature.getXidget(): null;
      
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

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IXidgetFeature.class) return (T)this;
    return super.getFeature( clss);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IXidgetFeature#getXidget()
   */
  public IXidget getXidget()
  {
    return xidgets.peek();
  }

  private Class<? extends IXidget> xidgetClass;
  private Stack<IXidget> xidgets;
}
