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
   * only called if more than one tag handler is registered for a given tag.
   * @param processor The processor.
   * @param parent The parent handler.
   * @param element The element to be processed.
   * @return Returns true if this tag handler processes the specified element.
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element);
  
  /**
   * Process the specified element. If the method returns true then the tag processor
   * will push the children of the element onto its stack for processing. The tag
   * processor keeps track of the handler which processed the nearest ancestor of
   * the specified element.
   * @param processor The processor.
   * @param parent The parent handler.
   * @param element The element.
   * @return Returns true if processor should process the children of the element.
   */
  public boolean process( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException;
}
