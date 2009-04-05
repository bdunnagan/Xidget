/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column;

import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.feature.IIconFeature;
import org.xidget.text.TextXidget;
import org.xidget.text.feature.ITextModelFeature;
import org.xidget.text.feature.ITextWidgetFeature;
import org.xmodel.IModelObject;

/**
 * An implementation of IXidget for the columns of a table.
 */
public abstract class ColumnXidget extends TextXidget
{
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    // get features
    textModelFeature = getTextModelFeature();
    textWidgetFeature = getTextWidgetFeature();
    iconFeature = getIconFeature();
    
    return true;
  }  
  
  /**
   * Returns the implementation of ITextModelFeature.
   * @return Returns the implementation of ITextModelFeature.
   */
  protected abstract ITextModelFeature getTextModelFeature();
  
  /**
   * Returns the implementation of ITextWidgetFeature.
   * @return Returns the implementation of ITextWidgetFeature.
   */
  protected abstract ITextWidgetFeature getTextWidgetFeature();
  
  /**
   * Returns the implementation of IIconFeature.
   * @return Returns the implementation of IIconFeature.
   */
  protected abstract IIconFeature getIconFeature();
  
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss.equals( ITextModelFeature.class)) return (T)textModelFeature;
    if ( clss.equals( ITextWidgetFeature.class)) return (T)textWidgetFeature;
    if ( clss.equals( IIconFeature.class)) return (T)iconFeature;
    return super.getFeature( clss);
  }

  private ITextModelFeature textModelFeature;
  private ITextWidgetFeature textWidgetFeature;
  private IIconFeature iconFeature;
}
