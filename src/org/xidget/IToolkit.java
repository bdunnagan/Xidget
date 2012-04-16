/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IToolkit.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget;

import java.util.List;
import org.xidget.config.TagProcessor;
import org.xmodel.caching.IFileAssociation;
import org.xmodel.compress.ISerializer;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for platform-specific behavior.
 */
public interface IToolkit extends IFeatured
{
  public enum Confirmation { yes, no, cancel};
  
  /**
   * Configure the specified tag processor for parsing a xidget configuration file for a specific platform.
   * The implementation should add whatever tag handlers are required to generate platform-specific
   * xidget implementations.
   * @param processor The tag processor.
   */
  public void configure( TagProcessor processor);
  
  /**
   * @return Returns the names of all the available font families.
   */
  public List<String> getFonts();
  
  /**
   * Returns the implementation of IFileAssociation for loading images in a platform-specific way.
   * @return Returns the implementation of IFileAssociation for loading images in a platform-specific way.
   */
  public IFileAssociation getImageFileAssociation();

  /**
   * @return Returns an instance of ISerializer that handles images.
   */
  public ISerializer getImageSerializer();
  
  /**
   * Open a confirmation dialog.
   * @param context The context.
   * @param title The dialog title.
   * @param message The confirmation message.
   * @param allowCancel True if options include cancelling.
   * @return Returns the confirmation status.
   */
  public Confirmation openConfirmDialog( StatefulContext context, String title, Object image, String message, boolean allowCancel);
  
  public enum MessageType { error, warning, information, status};
  
  /**
   * Open a message dialog.
   * @param context The context.
   * @param title The dialog title.
   * @param message The confirmation message.
   * @param type The type of message.
   */
  public void openMessageDialog( StatefulContext context, String title, Object image, String message, MessageType type);
  
  public enum FileDialogType { openOne, openMany, save};
  
  /**
   * Open a file dialog.
   * @param context The context.
   * @param dir The current directory.
   * @param filter An expression that filters the files that can be selected.
   * @param dfault The default selected value.
   * @param desc A description of the file filter.
   * @param type The type of file chooser.
   * @return Returns an array containing the paths of the selected files.
   */
  public String[] openFileDialog( StatefulContext context, IExpression dir, IExpression filter, String dfault, String desc, FileDialogType type);
}
