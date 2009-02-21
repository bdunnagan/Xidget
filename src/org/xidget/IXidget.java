/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import org.xidget.layout.IAnchor;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for widget adapters.
 */
public interface IXidget
{
  /**
   * Set the parent of this xidget.
   * @param parent The parent.
   */
  public void setParent( IXidget parent);
  
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
   * Returns null or a tag handler for this xidget. The tag handler provides the
   * mechanism for this xidget to get its configuration. If a xidget does not
   * return a tag handler then it doesn't require configuration.
   * @return Returns null or a tag handler.
   */
  public XidgetTagHandler getTagHandler();
  
  /**
   * Returns the anchor with the specified name (e.g. top, left, right, bottom).
   * The reason the argument is a string instead of an enum is that it is left
   * to the implementation to decide what types of anchors it supports.
   * @param name The name of the anchor.
   * @return Returns the anchor with the specified name.
   */
  public IAnchor getAnchor( String name);
  
  /**
   * Display the specified content value error. A content value error is generated
   * when the content of the xidget does not pass schema value validation.
   * @param text The content value error.
   */
  public void showValueError( String text);
  
  /**
   * Display a content structure error. A content structure error is generated
   * when the content of the xidget does not have the correct schematic structure.
   * @param text The structure error.
   */
  public void showStructureError( String text);
}
