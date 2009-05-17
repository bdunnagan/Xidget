/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;

/**
 * The binding rule for any element containing a script. The script name is the element name.
 */
public class ScriptTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature xidgetFeature = (parent != null)? parent.getFeature( IXidgetFeature.class): null;
    return xidgetFeature != null && xidgetFeature.getXidget().getFeature( IScriptFeature.class) != null; 
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = (parent != null)? parent.getFeature( IXidgetFeature.class): null;
    IScriptFeature scriptFeature = xidgetFeature.getXidget().getFeature( IScriptFeature.class);

    ScriptAction script = new ScriptAction( processor.getClassLoader(), element);    
    scriptFeature.setScript( element.getType(), script);
    
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    return null;
  }
}
