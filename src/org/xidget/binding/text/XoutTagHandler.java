/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding.text;

import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.ifeature.text.ITextWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A tag handler for the <i>xin</i> element.
 */
public class XoutTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    // get transform
    IExpression xoutExpr = Xlate.get( element, (IExpression)null);
    
    IXidget xidget = xidgetFeature.getXidget();
    ITextWidgetFeature feature = xidget.getFeature( ITextWidgetFeature.class);
    feature.setTransform( ITextModelFeature.allChannel, xoutExpr);
    
    return false;
  }
}
