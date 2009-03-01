/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A tag handler which creates a new XidgetBinding on its parent xidget.
 */
public class BindingTagHandler implements ITagHandler
{
  /**
   * Create with the specified rule for creating the listener part of the binding.
   * @param rule The rule.
   */
  public BindingTagHandler( IBindingRule rule)
  {
    this.rule = rule;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( !(parent instanceof XidgetTagHandler))
      throw new TagException(
        "BindingTagHandler must occur as child of XidgetTagHandler.");

    // create expression
    String xpath = Xlate.get( element, "");
    if ( xpath.length() == 0)
      throw new TagException(
        "Empty expression in binding.");
    
    IExpression expression = XPath.createExpression( xpath);
    
    // create binding
    IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
    IExpressionListener listener = rule.getListener( xidget);
    XidgetBinding binding = new XidgetBinding( expression, listener);
    xidget.addBinding( binding);
    
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }
    
  private IBindingRule rule;
}
