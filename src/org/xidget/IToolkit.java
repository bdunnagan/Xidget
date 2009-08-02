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
  
  /**
   * Open a file dialog.
   * @param xidget The parent xidget.
   * @param context The context.
   * @param filter An expression that filters the files that can be selected.
   * @param description A description of the file filter.
   * @param multiSelect True if multiple files can be selected.
   * @return Returns an array containing the paths of the selected files.
   */
  public String[] openFileDialog( IXidget xidget, StatefulContext context, IExpression filter, String description, boolean multiSelect);
}
