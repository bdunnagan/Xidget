/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text.adapter;

import org.xidget.text.ITextValidator;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IModelTextAdapter which supports a variable number of channels.
 */
public class ModelTextAdapter implements IModelTextAdapter
{
  /**
   * Add a channel.
   * @param channel The name of the channel.
   */
  public void addChannel( String channel)
  {
  }
  
  /**
   * Remove the specified channel.
   * @param channel The name of the channel.
   */
  public void removeChannel( String channel)
  {
  }
    
  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#getSource(java.lang.String)
   */
  public IModelObject getSource( String channel)
  {
    // TODO Auto-generated method stub
    return null;
  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setEditable(boolean)
   */
  public void setEditable( boolean editable)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setSource(java.lang.String, org.xmodel.IModelObject)
   */
  public void setSource( String channel, IModelObject node)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setText(java.lang.String, java.lang.String)
   */
  public void setText( String channel, String text)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setTransform(java.lang.String, org.xmodel.xpath.expression.IExpression)
   */
  public void setTransform( String channel, IExpression transform)
  {
    // TODO Auto-generated method stub

  }

  /* (non-Javadoc)
   * @see org.xidget.text.adapter.IModelTextAdapter#setValidator(java.lang.String, org.xidget.text.ITextValidator)
   */
  public void setValidator( String channel, ITextValidator validator)
  {
    // TODO Auto-generated method stub

  }
}
