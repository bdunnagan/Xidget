/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
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
   * Called when the start tag of the configuration is encountered - before children.
   * @param processor The tag processor.
   * @param parent Null or the parent of this xidget.
   * @param element The configuration element.
   * @return Returns true if the tag processor should process the children of the specified element.
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException;
  
  /**
   * Called when the end tag of the configuration is encountered - after children.
   * @param processor The tag processor.
   * @param element The configuration element.
   */
  public void endConfig( TagProcessor processor, IModelObject element) throws TagException;

  /**
   * Returns null or an instance of the specified interface.
   * @param clss The interface class.
   * @return Returns null or an instance of the specified interface.
   */
  public Object getAdapter( Class<? extends Object> clss);  
}
