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
import org.xidget.ifeature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A tag handler which creates a new XidgetBinding on its parent xidget.
 */
public class BindingTagHandler extends AbstractTagHandler
{
  public static enum BindAt { start, end};
  
  /**
   * Create the tag handler with the specified rule and bind on the start-tag.
   * @param rule The rule.
   */
  public BindingTagHandler( IBindingRule rule)
  {
    this( rule, BindAt.start);
  }
  
  /**
   * Create the tag handler with the specified rule and optionally bind on end-tag,
   * meaning that the bind operation happens when the end-tag is visited. Binding
   * on the end-tag is useful when it is necessary that all xidget children be 
   * bound first.
   * @param rule The rule.
   * @param bindAt Indicate whether to bind on start-tag or end-tag.
   */
  public BindingTagHandler( IBindingRule rule, BindAt bindAt)
  {
    this.rule = rule;
    this.bindAt = bindAt;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( bindAt == BindAt.start) bind( processor, parent, element);      
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    if ( bindAt == BindAt.end) bind( processor, parent, element);
  }
  
  /**
   * Perform the bind operation.
   * @param processor The tag processor.
   * @param parent The parent tag handler.
   * @param element The configuration element.
   */
  private void bind( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    // check if rule applies
    IXidget xidget = xidgetFeature.getXidget();
    if ( !rule.applies( xidget, element)) return; 
    
    // create expression
    String xpath = Xlate.get( element, "");
    if ( xpath.length() == 0)
      throw new TagException(
        "Empty expression in binding.");
    
    IExpression expression = XPath.createExpression( xpath);
    
    // create binding
    IExpressionListener listener = rule.getListener( xidget, element);
    XidgetBinding binding = new XidgetBinding( expression, listener);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.add( binding);
  }

  private IBindingRule rule;
  private BindAt bindAt;
}
