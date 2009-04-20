/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.List;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for widget adapters.
 */
public interface IXidget extends IFeatured
{
  /**
   * Returns null or the parent of this xidget.
   * @return Returns null or the parent of this xidget.
   */
  public IXidget getParent();
  
  /**
   * Returns the children of this xidget.
   * @return Returns the children of this xidget.
   */
  public List<IXidget> getChildren();
  
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
   * Returns the configuration element that generated this xidget.
   * @return Returns the configuration element that generated this xidget.
   */
  public IModelObject getConfig();
  
  /**
   * Create the widget(s) associated with this xidget.
   */
  public void createWidget();
  
  /**
   * Destroy the widget(s) associated with this xidget.
   */
  public void destroyWidget();
  
  /**
   * Bind the xidget to the specified context.
   * @param context The context.
   */
  public void bind( StatefulContext context);
  
  /**
   * Unbind the xidget from the specified context.
   * @param context The context.
   */
  public void unbind( StatefulContext context);
}
