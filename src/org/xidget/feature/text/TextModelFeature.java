/*
 * Xidget - XML Widgets based on JAHM
 * 
 * TextModelFeature.java
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
package org.xidget.feature.text;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IOptionalNodeFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.text.ITextValidator;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITextModelAdapter which supports a variable number of channels.
 */
public class TextModelFeature implements ITextModelFeature
{  
  public TextModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    channels = new HashMap<String, Channel>();
    addChannel( allChannel);
    addChannel( selectedChannel);
  }
  
  /**
   * Add a channel.
   * @param channel The name of the channel.
   */
  public void addChannel( String channel)
  {
    channels.put( channel, new Channel());
  }
  
  /**
   * Remove the specified channel.
   * @param channel The name of the channel.
   */
  public void removeChannel( String channel)
  {
    channels.remove( channel);
  }
    
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#getSource(java.lang.String)
   */
  public IModelObject getSource( String channelName)
  {
    Channel channel = channels.get( channelName);
    return (channel != null)? channel.source: null;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( StatefulContext context, String channelName, IModelObject node)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null) 
    {
      channel.source = node;
      
      // validate
      String text = Xlate.get( node, "");
      IErrorFeature errorFeature = xidget.getFeature( IErrorFeature.class);
      if ( errorFeature != null && channel.validator != null)
      {
        String error = channel.validator.validate( text);
        if ( error != null) errorFeature.valueError( error);
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( StatefulContext context, String channelName, String text)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null && !channel.updating) 
    {
      //
      // Create or delete optional nodes depending on whether text is empty.
      // Note that the optional element is discarded without setting its value.
      //
      IOptionalNodeFeature optionalNodeFeature = xidget.getFeature( IOptionalNodeFeature.class);
      if ( optionalNodeFeature != null)
      {
        IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
        if ( channel.source == null)
        {
          if ( text.length() > 0) optionalNodeFeature.createOptionalNodes( channelName, bindFeature.getBoundContext());
        }
        else if ( channel.source != null)
        {
          if ( text.length() == 0) optionalNodeFeature.deleteOptionalNodes( channelName, bindFeature.getBoundContext());
        }
      }
      
      // update
      if ( channel.source != null)
      {
        // transform
        if ( channel.transform != null) 
          text = channel.transform.transform( context, text);
        
        // validate
        IErrorFeature errorFeature = xidget.getFeature( IErrorFeature.class);
        if ( errorFeature != null && channel.validator != null)
        {
          String error = channel.validator.validate( text);
          if ( error != null) errorFeature.valueError( error);
        }
        
        // commit
        try
        {
          channel.updating = true;
          channel.source.setValue( text);
        }
        finally
        {
          channel.updating = false;
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channelName, IExpression expression)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null) channel.transform = new TextTransform( expression);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setValidator(java.lang.String, org.xidget.text.ITextValidator)
   */
  public void setValidator( String channelName, ITextValidator validator)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null) channel.validator = validator;
  }
  
  private class Channel
  {
    IModelObject source;
    TextTransform transform;
    ITextValidator validator;
    boolean updating;
  }

  private IXidget xidget;
  private Map<String, Channel> channels;
}
