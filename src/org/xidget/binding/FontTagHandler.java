/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xmodel.IModelObject;

/**
 * A customization of BindingTagHandler that allows font element attributes to define binding rules for the 
 * individual font characteristics (i.e. family, styles, size).
 */
public class FontTagHandler extends BindingTagHandler
{
  public FontTagHandler()
  {
    super( new FontBindingRule());
  }

  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#getFeature(java.lang.Class)
   */
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IXidgetFeature.class) return parent.getFeature( clss);
    return super.getFeature( clss);
  }

  /* (non-Javadoc)
   * @see org.xidget.binding.BindingTagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    this.parent = parent;
    super.enter( processor, parent, element);
    return true;
  }
  
  private ITagHandler parent;
}
