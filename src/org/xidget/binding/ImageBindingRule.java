/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IconBindingRule.java
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

import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IImageFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * An implementation of IBindingRule for images.
 */
public class ImageBindingRule implements IBindingRule
{
  public enum Purpose { up, down, hover};
  
  public ImageBindingRule( Purpose purpose)
  {
    this.purpose = purpose;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return xidget.getFeature( IImageFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }  

  private final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = nodes.get( 0);
      IImageFeature feature = xidget.getFeature( IImageFeature.class);
      Object object = node.getValue();
      switch( purpose)
      {
        case up:    feature.setImage( object); break;
        case down:  feature.setImagePress( object); break;
        case hover: feature.setImageHover( object); break;
      }
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      IImageFeature feature = xidget.getFeature( IImageFeature.class);
      Object object = (node == null)? null: node.getValue();
      switch( purpose)
      {
        case up:    feature.setImage( object); break;
        case down:  feature.setImagePress( object); break;
        case hover: feature.setImageHover( object); break;
      }
    }

    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( object == node) 
      {
        IImageFeature feature = xidget.getFeature( IImageFeature.class);
        switch( purpose)
        {
          case up:    feature.setImage( newValue); break;
          case down:  feature.setImagePress( newValue); break;
          case hover: feature.setImageHover( newValue); break;
        }
      }
    }

    public boolean requiresValueNotification()
    {
      return true;
    }

    private IXidget xidget;
    private IModelObject node;
  }
  
  private Purpose purpose;
}
