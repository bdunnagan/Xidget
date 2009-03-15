/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.config.util.Pair;
import org.xidget.text.feature.IModelTextFeature;
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
  public final static String selectedChannel = "selected";
  
  protected TextXidget()
  {
    modelAdapter = new ModelTextFeature( this);
  }
  
  /**
   * Add a channel.
   * @param channel The name of the channel.
   */
  public void addChannel( String channel)
  {
  }
  
  /**
   * Remove the specified channel.
   * @param channel The name of the channel.
   */
  public void removeChannel( String channel)
  {
  }
      
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    super.startConfig( processor, parent, element);
    
    // create widget
    Pair size = new Pair( Xlate.get( element, "size", Xlate.childGet( element, "size", "")), 0, 0);
    createWidget( size);
    
    return true;
  }  

  /**
   * Create the widget with the specified configuration parameters. If the 
   * y-coordinate is 0 then the column size is unbounded. The row size will 
   * always be bounded.
   * @param size The size of the widget in characters.
   */
  protected abstract void createWidget( Pair size) throws TagException;
  
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IModelTextFeature.class) return (T)modelAdapter;
    return null;
  }

  private ModelTextFeature modelAdapter;
}