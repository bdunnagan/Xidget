/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Change.java
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
package org.xidget.selection;

/**
 * A change record calculated by one of the differs defined in this package. The change
 * record is used to update the selection.
 */
public class Change
{
  public int lIndex;
  public int rIndex;
  public int count;
}
