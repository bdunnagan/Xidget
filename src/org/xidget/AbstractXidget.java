/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.HashMap;
import java.util.Map;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.layout.IAnchor;
import org.xmodel.IModelObject;
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
  
  /**
   * Called with the configuration information for this xidget. This implementation does nothing.
   * @param processor The tag processor.
   * @param element The configuration element identified by tag processing.
   * @return Returns true if processing should descend into subtree.
   */
  protected boolean configure( TagProcessor processor, FormXidget parent, IModelObject element)
  {
    return false;
  }
  
  /**
   * Returns the tag to use for tag processing.
   * @return Returns the tag to use for tag processing.
   */
  protected abstract String getTag();

  /**
   * Returns a tag processor which handles parentage and calls the <code>configure</code> method.
   * This implementation can be overridden if more complex filtering is required.
   */
  public XidgetTagHandler getTagHandler()
  {
    return new XidgetTagHandler( getTag(), this) {
      public boolean process( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
      {
        super.process( processor, parent, element);
        return configure( processor, (FormXidget)parent, element);
      }
    };
  }

  private IXidget parent;
  private StatefulContext context;
  private Map<String, IAnchor> anchors;
}
