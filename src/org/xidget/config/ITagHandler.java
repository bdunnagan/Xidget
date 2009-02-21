/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import org.xmodel.IModelObject;

/**
 * An interface for processing an element from an xml configuration.
 */
public interface ITagHandler
{
  /**
   * Returns true if this tag handler processes the specified element. This method is
   * only called if more than one tag handler is registered for a given element name.
   * @param processor The processor.
   * @param element The element to be processed.
   * @return Returns true if this tag handler processes the specified element.
   */
  public boolean filter( TagProcessor processor, IModelObject element);
  
  /**
   * Process the specified element.
   * @param processor The processor.
   * @param element The element.
   * @return Returns false to stop tag processing.
   */
  public boolean process( TagProcessor processor, IModelObject element);
}
