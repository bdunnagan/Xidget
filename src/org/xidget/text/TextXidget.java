/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.text.feature.IModelTextFeature;
import org.xidget.text.feature.ModelTextFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag. Subclasses should usually export an IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{  
  public final static String allChannel = "all";
  
  protected TextXidget()
  {
    modelAdapter = new ModelTextFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    build( parent, element);
    return true;
  }  

  /**
   * Build the widget structure for this xidget.
   * @param parent The parent xidget.
   * @param element The configuration element.
   */
  protected abstract void build( IXidget parent, IModelObject element) throws TagException;
    
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IModelTextFeature.class) return (T)modelAdapter;
    return null;
  }

  private IModelTextFeature modelAdapter;
}