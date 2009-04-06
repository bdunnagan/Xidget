/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.ITextWidgetFeature;
import org.xidget.text.feature.TextModelFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag. Subclasses should usually export an IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{  
  public final static String allChannel = "all";
  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    // get features
    modelFeature = getModelTextFeature();
    widgetFeature = getWidgetFeature();
    textFeature = getWidgetTextFeature();
    errorFeature = getErrorFeature();
    
    return true;
  }  

  /**
   * Returns an implementation of IModelTextFeature. This method returns an instance
   * of ModelTextFeature. Subclasses can override to provide a different implementation.
   * @return Returns an implementation of IModelTextFeature.
   */
  protected ITextModelFeature getModelTextFeature()
  {
    return new TextModelFeature( this);
  }
  
  /**
   * Returns the required IWidgetFeature.
   * @return Returns the required IWidgetFeature.
   */
  protected abstract IWidgetFeature getWidgetFeature();
  
  /**
   * Returns the required IWidgetTextFeature.
   * @return Returns the required IWidgetTextFeature.
   */
  protected abstract ITextWidgetFeature getWidgetTextFeature();
  
  /**
   * Returns the required IErrorFeature.
   * @return Returns the required IErrorFeature.
   */
  protected abstract IErrorFeature getErrorFeature();
  
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == ITextModelFeature.class) return (T)modelFeature;
    if ( clss.equals( ITextWidgetFeature.class)) return (T)textFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;    
    return super.getFeature( clss);
  }

  private ITextModelFeature modelFeature;
  private ITextWidgetFeature textFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
}