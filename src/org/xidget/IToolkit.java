/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.TagProcessor;
import org.xmodel.external.caching.IFileAssociation;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for platform-specific behavior.
 */
public interface IToolkit
{
  /**
   * Configure the specified tag processor for parsing a xidget configuration file for a specific platform.
   * The implementation should add whatever tag handlers are required to generate platform-specific
   * xidget implementations.
   * @param processor The tag processor.
   */
  public void configure( TagProcessor processor);

  /**
   * Returns the implementation of IFileAssociation for loading images in a platform-specific way.
   * @return Returns the implementation of IFileAssociation for loading images in a platform-specific way.
   */
  public IFileAssociation getImageFileAssociation();

  public enum Confirmation { yes, no, cancel};
  
  /**
   * Open a confirmation dialog.
   * @param xidget The parent xidget.
   * @param context The context.
   * @param title The dialog title.
   * @param message The confirmation message.
   * @param allowCancel True if options include cancelling.
   * @return Returns the confirmation status.
   */
  public Confirmation openConfirmDialog( IXidget xidget, StatefulContext context, String title, Object image, String message, boolean allowCancel);
  
  public enum MessageType { error, warning, information, status};
  
  /**
   * Open a message dialog.
   * @param xidget The parent xidget.
   * @param context The context.
   * @param title The dialog title.
   * @param message The confirmation message.
   * @param type The type of message.
   */
  public void openMessageDialog( IXidget xidget, StatefulContext context, String title, Object image, String message, MessageType type);
  
  public enum FileDialogType { openOne, openMany, save};
  
  /**
   * Open a file dialog.
   * @param xidget The parent xidget.
   * @param context The context.
   * @param filter An expression that filters the files that can be selected.
   * @param description A description of the file filter.
   * @param type The type of file chooser.
   * @return Returns an array containing the paths of the selected files.
   */
  public String[] openFileDialog( IXidget xidget, StatefulContext context, IExpression filter, String description, FileDialogType type);
}
