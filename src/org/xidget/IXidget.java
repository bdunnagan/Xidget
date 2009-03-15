/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.List;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.layout.IComputeNode;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for widget adapters.
 */
public interface IXidget extends IFeatures
{
  /**
   * Returns the parent of this xidget.
   * @return Returns the parent of this xidget.
   */
  public IXidget getParent();

  /**
   * Returns the children of this xidget.
   * @return Returns the children of this xidget.
   */
  public List<IXidget> getChildren();
  
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
   * Bind this xidget.
   */
  public void bind();
  
  /**
   * Unbind this xidget.
   */
  public void unbind();
  
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
   * Add a binding to the xidget.
   * @param binding The binding.
   */
  public void addBinding( IXidgetBinding binding);
  
  /**
   * Remove a binding from the xidget.
   * @param binding The binding.
   */
  public void removeBinding( IXidgetBinding binding);

  /**
   * Returns the anchor of the specified type for this xidget as follows:
   * <ul>
   * <li><i>x0</i> - WidgetLeftNode 
   * <li><i>y0</i> - WidgetTopNode
   * <li><i>x1</i> - WidgetRightNode 
   * <li><i>y1</i> - WidgetBottomNode
   * <li><i>w</i> - WidgetWidthNode
   * <li><i>h</i> - WidgetHeightNode
   * </ul>
   * @param type The type of anchor.
   * @return Returns null or the widget anchor of the specified type.
   */
  public IComputeNode getAnchor( String type);
}
