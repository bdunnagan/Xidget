/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature.dialog;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for opening a dialog.
 */
public interface IDialogFeature
{
  /**
   * Open a dialog and wait to return until the dialog is closed.
   * @param context The dialog context.
   */
  public void open( StatefulContext context);
  
  /**
   * Close the dialog with the specified dialog context.
   * @param context The dialog context.
   */
  public void close( StatefulContext context);
}
