/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Cell.java
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
package org.xidget.tree;

import org.xidget.feature.tree.ColumnImageListener;
import org.xidget.feature.tree.ColumnSourceListener;
import org.xmodel.IModelObject;

/**
 * A data-structure for holding information about a table cell.
 */
public class Cell
{
  public Object icon;
  public Object value;
  public IModelObject source;
  public ColumnImageListener imageListener;
  public ColumnSourceListener sourceListener;
}
