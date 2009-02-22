/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.HashMap;
import java.util.Map;
import org.xidget.layout.IAnchor;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * Convenient base class for xidgets.
 */
public abstract class AbstractXidget implements IXidget
{
  protected AbstractXidget()
  {
    anchors = new HashMap<String, IAnchor>();
  }

  /**
   * Subclasses call this method to define their anchors.
   * @param name The name of the anchor.
   * @param anchor The anchor.
   */
  protected void addAnchor( String name, IAnchor anchor)
  {
    anchors.put( name, anchor);
  }
  
  /**
   * Subclass may call this method to remove an anchor.
   * @param name The name of the anchor.
   */
  protected void removeAnchor( String name)
  {
    anchors.remove( name);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#getAnchor(java.lang.String)
   */
  public IAnchor getAnchor( String name)
  {
    return anchors.get( name);
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setParent(org.xidget.IXidget)
   */
  public void setParent( IXidget parent)
  {
    this.parent = parent;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getParent()
   */
  public IXidget getParent()
  {
    return parent;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#setContext(org.xmodel.xpath.expression.StatefulContext)
   */
  public void setContext( StatefulContext context)
  {
    this.context = context;
  }

  /* (non-Javadoc)
   * @see org.xidget.IXidget#getContext()
   */
  public StatefulContext getContext()
  {
    return context;
  }
      
  private IXidget parent;
  private StatefulContext context;
  private Map<String, IAnchor> anchors;
}
