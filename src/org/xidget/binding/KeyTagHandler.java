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
import org.xmodel.xpath.expression.StatefulContext;

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
    if ( xidget.getFeature( IKeyFeature.class) == null)
    {
      throw new TagException( String.format( 
        "Key bindings not supported by xidget, %s.", xidget.getConfig().getType()));
    }
    
    // override?
    boolean override = Xlate.get( element, "override", true);
    
    // create expression
    String xpath = Xlate.get( element, "keys", "");
    if ( xpath.length() == 0) return;
    
    try
    {
      // create binding
      IExpression expression = XPath.compileExpression( xpath);
      
      XActionDocument doc = new XActionDocument( element);
      doc.addPackage( "org.xidget.xaction");
      doc.addPackage( "org.xidget.layout.xaction");
      doc.setClassLoader( processor.getClassLoader());
      IXAction script = doc.createScript();
        
      KeyBinding binding = new KeyBinding( expression, xidget, override, script);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    catch( PathSyntaxException e)
    {
      throw new TagException( String.format( "Error in expression: %s", element), e);
    }
  }
  
  private class KeyBinding implements IXidgetBinding
  {
    public KeyBinding( IExpression expression, IXidget xidget, boolean override, IXAction script)
    {
      this.expression = expression;
      this.listener = new Listener( xidget, override, script);
    }
    
    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
     */
    @Override
    public void bind( StatefulContext context)
    {
      expression.addNotifyListener( context, listener);
    }

    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
     */
    @Override
    public void unbind( StatefulContext context)
    {
      expression.removeNotifyListener( context, listener);
    }
    
    private IExpression expression;
    private Listener listener;
  }
  
  private class Listener extends ExpressionListener
  {
    public Listener( IXidget xidget, boolean override, IXAction script)
    {
      this.xidget = xidget;
      this.override = override;
      this.script = script;
    }
    
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      if ( oldValue == null) oldValue = "";
      if ( newValue == null) newValue = "";
      
      IKeyFeature keyFeature = xidget.getFeature( IKeyFeature.class);
      if ( !oldValue.equals( "")) keyFeature.unbind( oldValue, script);
      if ( !newValue.equals( "")) keyFeature.bind( newValue, override, script);
    }
    
    private IXidget xidget;
    private boolean override;
    private IXAction script;
  };
}
