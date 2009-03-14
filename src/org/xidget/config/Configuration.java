/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import java.util.ArrayList;
import java.util.List;
import org.xidget.BindingTagHandler;
import org.xidget.EnableBindingRule;
import org.xidget.IXidget;
import org.xidget.TooltipBindingRule;
import org.xidget.TriggerTagHandler;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.layout.LayoutTagHandler;
import org.xidget.text.EditableBindingRule;
import org.xidget.text.TextBindingRule;
import org.xidget.text.TextXidget;
import org.xmodel.IModelObject;

/**
 * The reference configuration parser which uses an IXidgetKit to obtain xidgets
 * for the appropriate widget platform, and installs all other platform-independent
 * tag handlers required to parse a xidget configuration file.
 */
public class Configuration
{
  /**
   * Create a configuration parser with the specified kit.
   * @param kit The xidget kit.
   */
  public Configuration( IXidgetKit kit)
  {
    // install xidget kit
    processor = new TagProcessor();
    processor.addFeature( new XidgetMap());
    
    addHandler( "form", kit.getFormHandler());
    addHandler( "button", kit.getButtonHandler());
    addHandler( "slider", kit.getSliderHandler());
    addHandler( "text", kit.getTextHandler());
    addHandler( "combo", kit.getComboHandler());
    addHandler( "table", kit.getTableHandler());
    addHandler( "tree", kit.getTreeHandler());

    // install other handlers
    addHandler( "layout", new LayoutTagHandler());
    addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    addHandler( "tooltip", new BindingTagHandler( new TooltipBindingRule()));
    addHandler( "editable", new BindingTagHandler( new EditableBindingRule( TextXidget.allChannel)));
    addHandler( "source", new BindingTagHandler( new TextBindingRule( TextXidget.allChannel)));
    addHandler( "trigger", new TriggerTagHandler());
  }
  
  /**
   * Parse the specified root of a xidget configuration.
   * @param root The root element.
   */
  public List<IXidget> parse( IModelObject root) throws TagException
  {
    List<Object> objects = processor.process( root);
    List<IXidget> xidgets = new ArrayList<IXidget>( objects.size());
    for( Object object: objects) xidgets.add( (IXidget)object);
    return xidgets;
  }
  
  /**
   * Add the specified handler to the tag processor.
   * @param tag The tag.
   * @param handler The handler.
   */
  public void addHandler( String tag, ITagHandler handler)
  {
    if ( handler != null) processor.addHandler( tag, handler);
  }
 
  /**
   * Remove the specified handler from the tag processor.
   * @param tag The tag.
   * @param handler The handler.
   */
  public void removeHandler( String tag, ITagHandler handler)
  {
    processor.removeHandler( tag, handler);
  }
  
  private TagProcessor processor;
}