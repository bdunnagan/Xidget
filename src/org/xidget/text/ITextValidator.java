/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ITextValidator.java
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
package org.xidget.text;

/**
 * An interface for validating text when it is committed to the model.
 */
public interface ITextValidator
{
  /**
   * Validate the specified text and return null or a validation error string.
   * @param text The text to be validated.
   * @return Returns null or the validation error string.
   */
  public String validate( String text);
}
