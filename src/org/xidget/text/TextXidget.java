/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.text;

import org.xidget.AbstractXidget;
import org.xidget.IXidget;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.config.util.Size;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;

/**
 * An implementation of IXidget for use with the text widgets and other widgets
 * which have one input/output text value. A text xidget is registered for the
 * <i>text</i> tag. Subclasses should usually export an IErrorAdapter.
 */
public abstract class TextXidget extends AbstractXidget
{  
  /* (non-Javadoc)
   * @see org.xidget.IXidget#startConfig(org.xidget.config.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean startConfig( TagProcessor processor, IXidget parent, IModelObject element) throws TagException
  {
    setParent( parent);
   
    // create widget
    Size size = new Size( Xlate.childGet( element, "size", ""), 1, 0);
    IWidgetTextChannel widgetChannel = createWidget( size);
    channel = new TextChannel( this, widgetChannel);
    
    return true;
  }

  /**
   * Create the widget with the specified configuration parameters. If the 
   * y-coordinate is 0 then the column size is unbounded. The row size will 
   * always be bounded.
   * @param size The size of the widget in characters.
   * @return Returns a widget channel.
   */
  protected abstract IWidgetTextChannel createWidget( Size size) throws TagException;
    
  /* (non-Javadoc)
   * @see org.xidget.IAdaptable#getAdapter(java.lang.Class)
   */
  public Object getAdapter( Class<? extends Object> clss)
  {
    if ( clss == ITextChannelAdapter.class) return new ITextChannelAdapter() {
      public TextChannel getChannel( int index)
      {
        return channel;
      }
    };
    
    return null;
  }

  private TextChannel channel;
}