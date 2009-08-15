/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.IXidget;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;

/**
 * An extension of XidgetTagHandler for use with containers that offer the IDynamicContainerFeature.
 * This pattern does not create xidgets belonging to the ConfigurationSwitch for the dynamic components
 * while parsing. Instead, the ConfigurationSwitch switches the configuration elements and creates 
 * a new xidget form the matching configuration element.
 * <p>
 * Note the difference between this pattern and the ConfigurationSwitch used in tree processing. All
 * of the sub-tree xidgets are created during parsing and assembled later into a ConfigurationSwitch.
 * <p>
 * Tabs are an example of a dynamic container.
 */
public class DynamicContainerTagHandler extends XidgetTagHandler
{
  /**
   * Create a xidget tag handler which instantiates xidgets of the specified class.
   * @param xidgetClass The implementation class of IXidget.
   */
  public DynamicContainerTagHandler( Class<? extends IXidget> xidgetClass)
  {
    super( xidgetClass);
  }
    
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    // ignore all switched xidgets
    if ( element.getAttribute( "when") != null || element.getFirstChild( "when") != null) return false;
    
    // process configuration otherwise
    return super.enter( processor, parent, element);
  }
}
