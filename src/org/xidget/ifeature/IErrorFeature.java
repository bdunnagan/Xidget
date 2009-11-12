/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IErrorFeature.java
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
package org.xidget.ifeature;

/**
 * An interface for reporting a xidget data error.
 */
public interface IErrorFeature
{
  /**
   * Called when the xidget data contains a value error.
   * @param message The error message.
   */
  public void valueError( String message);
  
  /**
   * Called when the xidget data contains a structure error.
   * @param message The error message.
   */
  public void structureError( String message);
}
