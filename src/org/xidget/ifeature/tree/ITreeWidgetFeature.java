/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ITreeWidgetFeature.java
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
package org.xidget.ifeature.tree;

import java.util.List;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for operating on the widget of a tree xidget. This interface assumes
 * that the widget supports multi-column trees, also known as table-trees.  Note that
 * the interface specifies a locus with a single instance of Row.  This is because
 * the Row data-structure holds a reference to its parent and thus the complete path
 * of the node can be determined.
 */
public interface ITreeWidgetFeature
{
  /**
   * Insert rows into the table. This method should not fire an update event. The
   * <code>commit</code> method will be called after all the updates have been
   * made and it should fire update events.  Only a single parent row will be 
   * modified before a call to <code>commit</code>.
   * @param parent The parent row.
   * @param rowIndex The index of the first row.
   * @param rows The rows to be inserted.
   */
  public void insertRows( Row parent, int rowIndex, Row[] rows);
  
  /**
   * Remove rows from the table. This method should not fire an update event. The
   * <code>commit</code> method will be called after all the updates have been
   * made and it should fire update events.  Only a single parent row will be 
   * modified before a call to <code>commit</code>.
   * @param parent The parent row.
   * @param rowIndex The index of the first row.
   * @param rows The rows that were removed.
   * @param dummy True if the dummy/temporary node is being removed.
   */
  public void removeRows( Row parent, int rowIndex, Row[] rows, boolean dummy);
  
  /**
   * Fire update events. This method is called after all changes to the table rows
   * have been made through the <code>insertRows</code> and <code>removeRows</code>
   * methods.
   * @param parent The parent row.
   */
  public void commit( Row parent);
  
  /**
   * Returns the children of the specified row.
   * @param parent The row.
   * @return Returns the children of the specified row.
   */
  public List<Row> getChildren( Row parent);
  
  /**
   * Returns the row with the specified context instance.
   * @param context The context instance.
   * @return Returns the row with the specified context instance.
   */
  public Row findRow( StatefulContext context);
  
  /**
   * Returns true if the specified row is visible in the tree.
   * @param row The row.
   * @return Returns true if the specified row is visible in the tree.
   */
  public boolean isVisible( Row row);
  
  /**
   * Set the title of the specified column.
   * @param columnIndex The column index.
   * @param title The title.
   */
  public void setTitle( int columnIndex, String title);

  /**
   * Update the specified cell.
   * @param row The row.
   * @param columnIndex The column index.
   */
  public void updateCell( Row row, int columnIndex);
}
