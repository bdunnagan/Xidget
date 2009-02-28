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
   * Returns the named text channel.
   * @param name The name of the channel.
   * @return Returns the named text channel.
   */
  public TextChannel getChannel( String name);
}
