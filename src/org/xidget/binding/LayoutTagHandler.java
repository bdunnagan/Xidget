/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITagHandler for configuring the layout of a xidget. This handler
 * can be used as both an attribute and element handler.
 */
public class LayoutTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");
    
    IXidget xidget = xidgetFeature.getXidget();
    if ( xidget == null) throw new TagException( "Parent tag handler is not a xidget: "+element);

    // quietly return if there is not layout feature
    ILayoutFeature feature = xidget.getFeature( ILayoutFeature.class);
    if ( feature == null) return false;
    
    // assign layout
    if ( element.getValue() == null)
    {
      // declared in-place
      feature.configure( processor, element);      
    }
    else
    {
      // referenced
      try
      {
        IExpression layoutExpr = XPath.createExpression( Xlate.get( element, (String)null));
        IModelObject layout = layoutExpr.queryFirst( new StatefulContext( processor.getContext(), element));
        feature.configure( processor, layout);
      }
      catch( Exception e)
      {
        throw new TagException( "Unable to parse layout for element: "+element, e);
      }
    }
    
    return false;
  }
}
