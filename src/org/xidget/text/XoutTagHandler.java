/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.IXidget;
import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.text.feature.ITextModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A tag handler for the <i>xin</i> element.
 */
public class XoutTagHandler implements ITagHandler
{
  /**
   * Create a tag handler associated with the specified text channel.
   * @param channel The text channel.
   */
  public XoutTagHandler( String channel)
  {
    this.channel = channel;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( !(parent instanceof XidgetTagHandler))
      throw new TagException(
        "XoutTagHandler must occur as child of XidgetTagHandler.");

    // get transform
    IExpression xoutExpr = Xlate.get( element, (IExpression)null);
    
    IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
    ITextModelFeature adapter = xidget.getFeature( ITextModelFeature.class);
    adapter.setTransform( channel, xoutExpr);
    
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
  
  private String channel;
}
