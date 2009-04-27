/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding.table;

import org.xidget.binding.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagProcessor;
import org.xidget.table.SubTableXidget;
import org.xmodel.IModelObject;

/**
 * A XidgetTagHandler which handles nested table groups.
 */
public class SubTableTagHandler extends XidgetTagHandler
{
  public SubTableTagHandler()
  {
    super( SubTableXidget.class);
  }

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IModelObject parentElement = element.getParent();
    return parentElement.isType( "table") || parentElement.isType( "tree");
  }
}
