/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.dialog;

/**
 * An interface for opening a dialog.
 */
public interface IDialogFeature
{
  /**
   * Open a dialog and wait to return until the dialog is closed.
   */
  public void open();
}
