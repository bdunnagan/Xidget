package org.xidget.binding;

import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An implementation of ITagHandler that simply ignores the specified element. By default,
 * the tag processor will process all of the descendants of a xidget configuration. Sometimes
 * there are configuration elements within a xidget schema that do not require tag processing.
 */
public class IgnoreElementHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    return false;
  }
}
