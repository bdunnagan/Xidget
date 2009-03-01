/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.IXidget;
import org.xidget.XidgetTagHandler;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A tag handler for the <i>xin</i> element.
 */
public class XinTagHandler implements ITagHandler
{
  /**
   * Create a tag handler associated with the specified text channel.
   * @param index The text channel.
   */
  public XinTagHandler( int index)
  {
    this.index = index;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( !(parent instanceof XidgetTagHandler))
      throw new TagException(
        "XinTagHandler must occur as child of XidgetTagHandler.");

    // get xin expression
    IExpression xinExpr = Xlate.get( element, (IExpression)null);
    
    IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
    ITextChannelAdapter adapter = (ITextChannelAdapter)xidget.getAdapter( ITextChannelAdapter.class);
    TextChannel channel = adapter.getChannel( this.index);
    channel.setInputTransform( xinExpr);    
    
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#exit(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#filter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }
  
  private int index;
}
