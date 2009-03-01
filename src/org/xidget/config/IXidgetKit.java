/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import org.xidget.config.processor.ITagHandler;

/**
 * An interface for accessing all of the tag handlers required for a complete implementation
 * of the xidget tag set. The existence of this interface does not preclude partial implementations
 * of the tag set.
 */
public interface IXidgetKit
{
  /**
   * Returns the handler for the <i>button</i> element.
   * @return Returns the handler for the <i>button</i> element.
   */
  public ITagHandler getButtonHandler();
  
  /**
   * Returns the handler for the <i>slider</i> element.
   * @return Returns the handler for the <i>slider</i> element.
   */
  public ITagHandler getSliderHandler();
  
  /**
   * Returns the handler for the <i>text</i> element.
   * @return Returns the handler for the <i>text</i> element.
   */
  public ITagHandler getTextHandler();

  /**
   * Returns the handler for the <i>combo</i> element.
   * @return Returns the handler for the <i>combo</i> element.
   */
  public ITagHandler getComboHandler();
  
  /**
   * Returns the handler for the <i>table</i> element.
   * @return Returns the handler for the <i>table</i> element.
   */
  public ITagHandler getTableHandler();
  
  /**
   * Returns the handler for the <i>tree</i> element.
   * @return Returns the handler for the <i>tree</i> element.
   */
  public ITagHandler getTreeHandler();
}
