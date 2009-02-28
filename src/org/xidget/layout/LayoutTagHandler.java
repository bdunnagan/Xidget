/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import org.xidget.IXidget;
import org.xidget.XidgetTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.util.Radix;

/**
 * A class which creates a layout for its components from the configuration.
 * <layout name="l1">
 *   <x0>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</x0>
 *   <y0>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</y0>
 *   <x1>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</x1>
 *   <y1>[[p|n].[x0|x1|y0|y1|c]][(+/-)N]</y1>
 * </layout>
 * <p>
 * This class handles tags for both layout declarations and layout references which
 * take the form of an element named <i>layout</i> whose value is the name of the
 * layout. 
 */
public class LayoutTagHandler implements ITagHandler
{  
  public LayoutTagHandler( LayoutRegistry registry)
  {
    this.registry = registry;
  }
    
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    String reference = Xlate.get( element, "");
    if ( reference.length() == 0)
    {
      Layout layout = new Layout();
      
      String text = Xlate.childGet( element, "x0", (String)null);
      if ( text != null) layout.x0 = createAttachment( text);
      
      text = Xlate.childGet( element, "y0", (String)null);
      if ( text != null) layout.y0 = createAttachment( text); 
  
      text = Xlate.childGet( element, "x1", (String)null);
      if ( text != null) layout.x1 = createAttachment( text); 
  
      text = Xlate.childGet( element, "y1", (String)null);
      if ( text != null) layout.y1 = createAttachment( text);
      
      String name = Xlate.get( element, "name", (String)null);
      if ( name == null)
      {
        if ( !(parent instanceof XidgetTagHandler))
          throw new TagException( 
            "Unnamed layout declaration not associated with xidget.");

        // generate unique key to associate xidget with its layout
        IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
        String key = Radix.convert( xidget.hashCode());
        registry.associateLayout( xidget, key);
        registry.defineLayout( key, layout);
      }
      else
      {
        registry.defineLayout( name, layout);
      }
    }
    else
    {
      if ( !(parent instanceof XidgetTagHandler))
        throw new TagException( 
          "Layout reference not associated with xidget.");
      
      IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
      registry.associateLayout( xidget, reference);
    }
    
    return false;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }
  
  /**
   * Parse the specified text into a layout coordinate.
   * @param text The text.
   */
  private Attachment createAttachment( String text) throws TagException
  {
    Attachment coordinate = new Attachment();
    
    char attach = text.charAt( 0);
    if ( attach != 'p' && attach != 'n') 
      throw new TagException( "Illegal: first character must be 'p' or 'n'.");
    
    coordinate.previous = (attach == 'p');
    
    int index = text.indexOf( '.') + 1;
    char letter = text.charAt( index);
    if ( letter != 'x' && letter != 'y')
      throw new TagException( "Illegal: coordinate must follow period."); 

    char number = text.charAt( index++);
    if ( number != '0' && number != '1')
      throw new TagException( "Illegal: coordinate must follow period."); 

    if ( letter == 'x')
    {
      coordinate.relative = (number == '0')? Relative.x0: Relative.x1; 
    }
    else
    {
      coordinate.relative = (number == '0')? Relative.y0: Relative.y1; 
    }
    
    try
    {
      coordinate.offset = Integer.parseInt( text.substring( index));
    }
    catch( NumberFormatException e)
    {
      throw new TagException( "Illegal: coordinate offset is not an integer.", e);
    }
    
    return coordinate;
  }
    
  public enum Relative { x0, y0, x1, y1, center};

  public class Attachment
  {
    public boolean previous;
    public Relative relative;
    public int offset;
  }
  
  public class Layout
  {
    public Attachment x0;
    public Attachment y0;
    public Attachment x1;
    public Attachment y1;
  }
  
  private LayoutRegistry registry;
}
