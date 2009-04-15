/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.feature.IErrorFeature;
import org.xidget.feature.IWidgetFeature;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.ITextWidgetFeature;
import org.xidget.text.feature.TextModelFeature;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag. Subclasses should usually export an IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{  
  public final static String allChannel = "all";
  
  /* (non-Javadoc)
   * @see org.xidget.AbstractXidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    super.createFeatures();
    
    modelFeature = getTextModelFeature();
    textFeature = getTextWidgetFeature();
    errorFeature = getErrorFeature();
    widgetFeature = getWidgetFeature();
  }

  /**
   * Returns an implementation of IModelTextFeature. This method returns an instance
   * of TextModelFeature. Subclasses can override to provide a different implementation.
   * @return Returns an implementation of IModelTextFeature.
   */
  protected ITextModelFeature getTextModelFeature()
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
  protected abstract ITextWidgetFeature getTextWidgetFeature();
  
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
    if ( clss == ITextWidgetFeature.class) return (T)textFeature;
    if ( clss == IErrorFeature.class) return (T)errorFeature;
    if ( clss == IWidgetFeature.class) return (T)widgetFeature;
    
    return super.getFeature( clss);
  }

  private ITextModelFeature modelFeature;
  private ITextWidgetFeature textFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
}