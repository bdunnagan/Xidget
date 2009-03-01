/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import org.xidget.BindingTagHandler;
import org.xidget.EnableBindingRule;
import org.xidget.config.processor.ITagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.text.EditableBindingRule;
import org.xidget.text.TextBindingRule;
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
    addHandler( "button", kit.getButtonHandler());
    addHandler( "slider", kit.getSliderHandler());
    addHandler( "text", kit.getTextHandler());
    addHandler( "combo", kit.getComboHandler());
    addHandler( "table", kit.getTableHandler());
    addHandler( "tree", kit.getTreeHandler());
    
    // install other handlers
    addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    addHandler( "editable", new BindingTagHandler( new EditableBindingRule( 0)));
    addHandler( "source", new BindingTagHandler( new TextBindingRule( 0)));
  }
  
  /**
   * Parse the specified root of a xidget configuration.
   * @param root The root element.
   */
  public void parse( IModelObject root) throws TagException
  {
    processor.process( root);
  }
  
  /**
   * Add the specified handler to the tag processor.
   * @param tag The tag.
   * @param handler The handler.
   */
  private void addHandler( String tag, ITagHandler handler)
  {
    if ( handler != null) processor.addHandler( tag, handler);
  }
  
  private TagProcessor processor;
}