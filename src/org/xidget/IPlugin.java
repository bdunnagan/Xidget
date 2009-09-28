/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

/**
 * An interface for classes that extend the capabilities of the an IToolkit. The IToolkit provides
 * the implementation of the base xidget set for a specific widget platform (e.g. Swing).  A plugin
 * adds one or more new xidgets and optionally provides one or more widget platform implementations.
 */
public interface IPlugin
{
  /**
   * Configure the plugin. This method should customize the TagProcessor of the toolkit to
   * recognize the xidgets and/or tags that are part of the service the plugin provides.
   * @param toolkit The toolkit.
   */
  public void configure( IToolkit toolkit);
}
