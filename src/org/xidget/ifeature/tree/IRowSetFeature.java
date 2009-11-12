/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IRowSetFeature.java
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
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for performing targeted updates to the rows of a table given 
 * a complete row-set. The implementation will typically perform a shallow
 * diff of the new row-set with the old row-set.
 */
public interface IRowSetFeature
{
  /**
   * Set the rows of the row-set.
   * @param context The parent context.
   * @param rows The row objects.
   */
  public void setRows( StatefulContext context, List<IModelObject> rows);
  
  /**
   * Returns the rows of the row-set.
   * @param context The parent context.
   * @return Returns the rows of the row-set.
   */
  public List<IModelObject> getRows( StatefulContext context);
}
