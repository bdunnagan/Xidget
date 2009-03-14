/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.HashMap;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.XidgetTagHandler;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xml.XmlIO;

/**
 * This class handles tags for both layout declarations and layout references which
 * take the form of an element named <i>layout</i> whose value is the name of the
 * layout. 
 */
public class LayoutTagHandler implements ITagHandler
{  
  public LayoutTagHandler()
  {
    declarations = new HashMap<String, IModelObject>();
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
    if ( !(parent instanceof XidgetTagHandler)) 
    {
      throw new TagException( "Layout not associated with xidget: "+XmlIO.toString( element));
    }
    
    String reference = Xlate.get( element, "");
    if ( reference.length() > 0)
    {
      IModelObject declaration = declarations.get( reference);
      if ( declaration == null) 
      {
        throw new TagException( "Declaration not found for layout with name: "+reference);
      }
      
      // reference to a layout
      setLayout( processor, parent, declaration);
    }
    else
    {
      // layout declaration
      String name = Xlate.get( element, "name", (String)null);
      if ( name != null) 
      {
        // save declaration
        declarations.put( name, element);
      }
      else
      {
        // declared in-place
        setLayout( processor, parent, element);
      }
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
   * Set the layout on the xidget associated with the specified tag handler.
   * @param processor The processor.
   * @param parent The tag handler.
   * @param declaration The layout declaration.
   */
  private void setLayout( TagProcessor processor, ITagHandler parent, IModelObject declaration)
  {
    IXidget xidget = ((XidgetTagHandler)parent).getLastXidget();
    IXidget container = xidget.getParent();
    if ( container != null)
    {
      ILayoutFeature feature = container.getFeature( ILayoutFeature.class);
      if ( feature != null) feature.setLayout( processor, xidget, declaration);
    }
  }
  
  private Map<String, IModelObject> declarations;
}
