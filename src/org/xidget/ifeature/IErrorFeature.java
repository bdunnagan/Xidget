/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

/**
 * An interface for reporting a xidget data error.
 */
public interface IErrorFeature
{
  /**
   * Called when the xidget data contains a value error.
   * @param message The error message.
   */
  public void valueError( String message);
  
  /**
   * Called when the xidget data contains a structure error.
   * @param message The error message.
   */
  public void structureError( String message);
}
