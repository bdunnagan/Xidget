/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

/**
 * An adapter for accessing instances of TextChannel on a xidget.
 */
public interface ITextChannelAdapter
{
  /**
   * Returns the specified channel.
   * @param index The channel index.
   * @return Returns the specified channel.
   */
  public TextChannel getChannel( int index);
}
