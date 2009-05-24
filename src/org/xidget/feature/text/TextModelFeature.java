/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.text;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.config.util.TextTransform;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.text.ITextModelFeature;
import org.xidget.text.ITextValidator;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IModelTextAdapter which supports a variable number of channels.
 */
public class TextModelFeature implements ITextModelFeature
{  
  public final static String allChannel = "all";
  public final static String selectedChannel = "selected";
  
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
  public void setSource( String channelName, IModelObject node)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null) channel.source = node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#getSource()
   */
  public IModelObject getSource()
  {
    return getSource( allChannel);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISourceFeature#setSource(org.xmodel.IModelObject)
   */
  public void setSource( IModelObject node)
  {
    setSource( allChannel, node);
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( String channelName, String text)
  {
    Channel channel = channels.get( channelName);
    if ( channel != null && channel.source != null && !channel.updating) 
    {
      // transform
      if ( channel.transform != null) 
        text = channel.transform.transform( text);
      
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