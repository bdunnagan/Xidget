/*
 * Xidget - XML Widgets based on JAHM
 * 
 * BindingTagHandler.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import org.xmodel.PathSyntaxException;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A tag handler which creates a new XidgetBinding on its parent xidget.
 */
public class BindingTagHandler extends AbstractTagHandler
{
  /**
   * Create the tag handler with the specified rule and bind on the start-tag. 
   * The binding will be performed after all child xidgets are bound.
   * @param rule The rule.
   */
  public BindingTagHandler( IBindingRule rule)
  {
    this( rule, false);
  }
  
  /**
   * Create the tag handler with the specified rule and bind on the start-tag.
   * The binding is performed before or after all child xidgets are bound.
   * @param rule The rule.
   * @param beforeChildren True if rule should be bound before xidget children are bound.
   */
  public BindingTagHandler( IBindingRule rule, boolean beforeChildren)
  {
    this.rule = rule;
    this.beforeChildren = beforeChildren;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    bind( processor, parent, element);      
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
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
    if ( xpath.length() == 0) return;
    
    try
    {
      // create binding
      IExpression expression = XPath.compileExpression( xpath);
      IExpressionListener listener = rule.getListener( xidget, element);
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      if ( beforeChildren) bindFeature.addBindingBeforeChildren( binding); 
      else bindFeature.addBindingAfterChildren( binding);
    }
    catch( PathSyntaxException e)
    {
      throw new TagException( String.format( "Error in expression: %s", element), e);
    }
  }

  private IBindingRule rule;
  private boolean beforeChildren;
}
