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
import org.xidget.text.feature.IModelTextFeature;
import org.xidget.text.feature.IWidgetTextFeature;
import org.xidget.text.feature.ModelTextFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

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
    
    // build 
    String label = Xlate.childGet( element, "label", (String)null);
    build( parent, label, element);
    
    // get features
    widgetFeature = getWidgetFeature();
    textFeature = getWidgetTextFeature();
    errorFeature = getErrorFeature();
    
    return true;
  }  

  /**
   * Build the widget structure for this xidget. The label should be visually represented 
   * in some widget-specific way.  The exact interpretation of the label is both widget
   * toolkit and locale -specific.
   * @param parent The parent xidget.
   * @param label The xidget label.
   * @param element The configuration element.
   */
  protected abstract void build( IXidget parent, String label, IModelObject element) throws TagException;

  /**
   * Returns the required IWidgetFeature.
   * @return Returns the required IWidgetFeature.
   */
  protected abstract IWidgetFeature getWidgetFeature();
  
  /**
   * Returns the required IWidgetTextFeature.
   * @return Returns the required IWidgetTextFeature.
   */
  protected abstract IWidgetTextFeature getWidgetTextFeature();
  
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
    if ( clss == IModelTextFeature.class) return (T)modelAdapter;
    if ( clss.equals( IWidgetTextFeature.class)) return (T)textFeature;
    if ( clss.equals( IWidgetFeature.class)) return (T)widgetFeature;
    if ( clss.equals( IErrorFeature.class)) return (T)errorFeature;    
    return super.getFeature( clss);
  }

  private IModelTextFeature modelAdapter;
  private IWidgetTextFeature textFeature;
  private IWidgetFeature widgetFeature;
  private IErrorFeature errorFeature;
}