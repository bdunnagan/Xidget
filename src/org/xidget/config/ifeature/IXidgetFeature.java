/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IXidgetFeature.java
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
package org.xidget.config.ifeature;

import org.xidget.IXidget;

/**
 * An interface for accessing an instance of IXidget created by an ITagHandler.
 * This feature is for use on ITagHandler.
 */
public interface IXidgetFeature
{
  /**
   * Returns the last xidget created by the ITagHandler.
   * @return Returns the last xidget created by the ITagHandler.
   */
  public IXidget getXidget();
}
