/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IColumnSetFeature.java
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
import org.xidget.tree.Column;
import org.xidget.tree.Row;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for binding the column set of a table.
 */
public interface IColumnSetFeature
{
  /**
   * Insert the specified row into the table.
   * @param row The row.
   * @param context The row context.
   * @return Returns the new row.
   */
  public void bind( Row row, StatefulContext context);
  
  /**
   * Remove the specified row from the table.
   * @param row The row.
   */
  public void unbind( Row row);
  
  /**
   * Returns the columns of the table.
   * @return Returns the columns of the table.
   */
  public List<Column> getColumns();
}
