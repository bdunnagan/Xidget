/*
 * Xidget - XML Widgets based on JAHM
 * 
 * XinTagHandler.java
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
package org.xidget.binding.text;

import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A tag handler for the <i>xin</i> element.
 */
public class XinTagHandler extends AbstractTagHandler
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
    IExpression xinExpr = Xlate.get( element, (IExpression)null);
    
    IXidget xidget = xidgetFeature.getXidget();
    ITextModelFeature feature = xidget.getFeature( ITextModelFeature.class);
    feature.setTransform( ITextModelFeature.allChannel, xinExpr);
    
    return false;
  }
}
