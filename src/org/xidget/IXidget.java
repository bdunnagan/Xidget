/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.TagProcessor;
import org.xidget.layout.IAnchor;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for widget adapters.
 */
public interface IXidget
{
  /**
   * Returns the parent of this xidget.
   * @return Returns the parent of this xidget.
   */
  public IXidget getParent();
    
  /**
   * Set the context.
   * @param context The context.
   */
  public void setContext( StatefulContext context);
  
  /**
   * Returns the context.
   * @return Returns the context.
   */
  public StatefulContext getContext();
  
  /**
   * Configure this xidget from the specified configuration.
   * @param processor The tag processor.
   * @param parent Null or the parent of this xidget.
   * @param element The configuration element.
   * @return Returns true if the tag processor should process the children of the specified element.
   */
  public boolean configure( TagProcessor processor, IXidget parent, IModelObject element);
  
  /**
   * Returns the anchor with the specified name (e.g. top, left, right, bottom).
   * The reason the argument is a string instead of an enum is that it is left
   * to the implementation to decide what types of anchors it supports.
   * @param name The name of the anchor.
   * @return Returns the anchor with the specified name.
   */
  public IAnchor getAnchor( String name);

  /**
   * Returns null or an instance of the specified interface.
   * @param clss The interface class.
   * @return Returns null or an instance of the specified interface.
   */
  public Object getAdapter( Class<? extends Object> clss);  
}
