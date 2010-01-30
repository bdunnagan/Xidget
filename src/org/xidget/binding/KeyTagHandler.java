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
import org.xidget.ifeature.IKeyFeature;
import org.xmodel.IModelObject;
import org.xmodel.PathSyntaxException;
import org.xmodel.Xlate;
import org.xmodel.xaction.IXAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

public class KeyTagHandler extends AbstractTagHandler
{
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

    IXidget xidget = xidgetFeature.getXidget();
    
    // create expression
    String xpath = Xlate.get( element, "keys", "");
    if ( xpath.length() == 0) return;
    
    try
    {
      // create binding
      IExpression expression = XPath.compileExpression( xpath);
      
      Listener listener = new Listener();
      listener.xidget = xidget;
      
      XActionDocument doc = new XActionDocument( element);
      doc.setClassLoader( processor.getClassLoader());
      listener.script = doc.createScript();
        
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    catch( PathSyntaxException e)
    {
      throw new TagException( String.format( "Error in expression: %s", element), e);
    }
  }
  
  private class Listener extends ExpressionListener
  {
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      if ( newValue == null) return;
      
      IKeyFeature keyFeature = xidget.getFeature( IKeyFeature.class);
      if ( keyFeature != null) keyFeature.bind( newValue, script);
    }
    
    IXidget xidget;
    IXAction script;
  };
}
