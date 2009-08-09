/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import org.xmodel.IModelObject;

/**
 * An interface for accessing the source element(s) of a xidget. The source element
 * stores the textual content of a xidget.  The source element is kept up-to-date
 * with the content according to the read/write semantics of the xidget.  For example,
 * a text xidget may either update the source element when a character is typed, or
 * after the enter key is pressed.
 * <p>
 * Some xidgets may have more than one source element, in which case the <i>channel</i>
 * name distinguishes them.
 */
public interface ISourceFeature
{
  public final static String allChannel = "all";
  public final static String selectedChannel = "selected";
  
  /**
   * Set the source node of the specified channel.
   * @param channel The source channel.
   * @param node The source node.
   */
  public void setSource( String channel, IModelObject node);
  
  /**
   * Get the source node of the specified channel.
   * @return Returns null or the source node of the specified channel.
   */
  public IModelObject getSource( String channel);
}
