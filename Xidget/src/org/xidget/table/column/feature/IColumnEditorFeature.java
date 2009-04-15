/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column.feature;

import org.xidget.IXidget;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for accessing the xidget editor for a column. All rows in the table
 * share the same column editor, and that editor must be bound to the row before it
 * can be used.  The implementation can choose to have an in-place widget editor or
 * a popup editor.
 */
public interface IColumnEditorFeature
{
  /**
   * Returns the xidget that defines the editor.
   * @return Returns the xidget that defines the editor.
   */
  public IXidget getEditor();
  
  /**
   * Use the editor to edit the specified context.
   * @param context The context.
   */
  public void openEditor( StatefulContext context);

  /**
   * Close the editor and optionally commit the changes.
   * @param context The context.
   * @param commit True if the changes should be committed. 
   */
  public void closeEditor( StatefulContext context, boolean commit);
}
