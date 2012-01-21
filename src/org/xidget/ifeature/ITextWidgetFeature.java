/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.EnumSet;

/**
 * An interface for behaviors specific to text widgets.
 */
public interface ITextWidgetFeature
{
  /**
   * Horizontal alignments.
   */
  public enum HAlign { left, center, right};
  
  /**
   * Set the horizontal text alignment for the widget.
   * @param alignment The alignment.
   */
  public void setHAlign( HAlign alignment);
  
  /**
   * Vertical alignments.
   */
  public enum VAlign { top, center, bottom};
  
  /**
   * Set the vertical text alignment for the widget.
   * @param alignment The alignment.
   */
  public void setVAlign( VAlign alignment);
  
  /**
   * Set whether the text is editable.
   * @param editable True if editable.
   */
  public void setEditable( boolean editable);

  /**
   * Font styles.
   */
  public enum FontStyle { plain, italic, bold};

  /**
   * Set the font family to the nearest matching font family.
   * @param family The font family.
   */
  public void setFontFamily( String family);
  
  /**
   * Set the font styles.
   * @param styles The styles.
   */
  public void setFontStyles( EnumSet<FontStyle> styles);
  
  /**
   * Set the font size. 
   * @param size The size.
   */
  public void setFontSize( double size);
}
