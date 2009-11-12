/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ITreeExpandFeature.java
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

import org.xidget.tree.Row;

/**
 * An interface for expanding and collapsing the data-model of a tree xidget.
 * This feature is used by the widget implementation to notify the data-model
 * that a tree node has been expanded or collapsed.  Since the data-model may
 * contain external references, these references should not be examined until
 * the tree explicitly requests it.
 */
public interface ITreeExpandFeature
{
  /**
   * Expand the children of the specified row.
   * @param row The row to be expanded.
   */
  public void expand( Row row);
  
  /**
   * Collapse the specified row.
   * @param row The row to be collapsed.
   */
  public void collapse( Row row);
  
  /**
   * Called when a row is added to a table. This method gives the feature
   * an opportunity to automatically expand the specified row.
   * @param row The row that was added.
   */
  public void rowAdded( Row row);
  
  /**
   * Called when a row is removed from a table. This method gives the feature
   * an opportunity to cleanup listeners.
   * @param row The row that was removed.
   */
  public void rowRemoved( Row row);
}
