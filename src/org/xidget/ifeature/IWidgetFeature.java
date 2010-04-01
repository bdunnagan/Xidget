/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IWidgetFeature.java
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

import org.xidget.layout.Bounds;
import org.xidget.layout.Margins;
import org.xidget.layout.Size;

/**
 * An interface for all widgets used by IXidget implementations. Instances of this interface
 * and its sub-interfaces are the contract by which IXidget implementations manipulate their
 * associated widgets.
 */
public interface IWidgetFeature
{
  /**
   * Set the bounds of the widget.
   * @param x The left-side x-coordinate.
   * @param y The top-side y-coordinate.
   * @param width The width of the widget.
   * @param height The height of the widget.
   */
  public void setBounds( float x, float y, float width, float height);
  
  /**
   * Returns the bounds of the widget.
   * @param result The result.
   */
  public void getBounds( Bounds result);
  
  /**
   * Returns the preferred size of the widget.
   * @param result The result.
   */
  public void getPreferredSize( Size result);
  
  /**
   * Returns the outside margins of the widget.
   * @return Returns the outside margins of the widget.
   */
  public Margins getOutsideMargins();
  
  /**
   * Set whether the widget is visible.
   * @param visible True if the widget is visible.
   */
  public void setVisible( boolean visible);
  
  /**
   * Returns true if the widget is visible.
   * @return Returns true if the widget is visible.
   */
  public boolean getVisible();
  
  /**
   * Set whether the widget is enabled.
   * @param enabled True if enabled.
   */
  public void setEnabled( boolean enabled);
  
  /**
   * Set the tooltip of the widget.
   * @param tooltip The tooltip.
   */
  public void setTooltip( String tooltip);
  
  /**
   * Set the foreground color of the widget.
   * @param color The foreground color.
   */
  public void setForeground( int color);
  
  /**
   * Set the background color of the widget.
   * @param color The background color.
   */
  public void setBackground( int color);
  
  /**
   * Set the name of the font family.
   * @param font The font family name (i.e. courier).
   */
  public void setFont( String font);
  
  /**
   * Set the font style.
   * @param style The font style name (i.e. italic).
   */
  public void setFontStyle( String style);
  
  /**
   * Set the font size.
   * @param size The size of the font in points.
   */
  public void setFontSize( double size);
}
