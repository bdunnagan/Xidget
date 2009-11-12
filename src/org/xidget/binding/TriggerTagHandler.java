/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TriggerTagHandler.java
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
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.trigger.EntityTrigger;
import org.xmodel.xaction.trigger.ITrigger;
import org.xmodel.xaction.trigger.SourceTrigger;
import org.xmodel.xaction.trigger.WhenTrigger;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITagHandler for triggers.
 */
public class TriggerTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.processor.ITagHandler#enter(org.xidget.config.processor.TagProcessor, 
   * org.xidget.config.processor.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");
    
    // create trigger
    ITrigger trigger = null;
    if ( element.getAttribute( "source") != null || element.getFirstChild( "source") != null)
      trigger = new SourceTrigger();
    else if ( element.getAttribute( "when") != null || element.getFirstChild( "when") != null)
      trigger = new WhenTrigger();
    else if ( element.getAttribute( "entity") != null || element.getFirstChild( "entity") != null)
      trigger = new EntityTrigger();
    
    // configure trigger
    XActionDocument document = new XActionDocument( processor.getClassLoader());
    document.setRoot( element);
    trigger.configure( document);
    
    TriggerBinding binding = new TriggerBinding( trigger);
    IXidget xidget = xidgetFeature.getXidget();
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    if ( bindFeature != null) bindFeature.addBindingAfterChildren( binding);
    
    return false;
  }

  private final static class TriggerBinding implements IXidgetBinding
  {
    TriggerBinding( ITrigger trigger)
    {
      this.trigger = trigger;
    }
    
    /* (non-Javadoc)
     * @see org.xidget.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void bind( StatefulContext context)
    {
      trigger.activate( context);
    }

    /* (non-Javadoc)
     * @see org.xidget.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void unbind( StatefulContext context)
    {
      trigger.deactivate( context);
    }
    
    private ITrigger trigger;
  }
}
