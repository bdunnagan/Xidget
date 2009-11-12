/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IDialogFeature.java
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
package org.xidget.ifeature.dialog;

import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for opening a dialog.
 */
public interface IDialogFeature
{
  /**
   * Open a dialog and wait to return until the dialog is closed.
   * @param context The dialog context.
   */
  public void open( StatefulContext context);
  
  /**
   * Close the dialog with the specified dialog context.
   * @param context The dialog context.
   */
  public void close( StatefulContext context);
}
