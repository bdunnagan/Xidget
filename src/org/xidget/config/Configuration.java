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
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
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
    
    if ( kit.getFormHandler() != null) processor.addHandler( "form", kit.getFormHandler());
    if ( kit.getButtonHandler() != null) processor.addHandler( "button", kit.getButtonHandler());
    if ( kit.getSliderHandler() != null) processor.addHandler( "slider", kit.getSliderHandler());
    if ( kit.getTextHandler() != null) processor.addHandler( "text", kit.getTextHandler());
    if ( kit.getComboHandler() != null) processor.addHandler( "combo", kit.getComboHandler());
    if ( kit.getTableHandler() != null) processor.addHandler( "table", kit.getTableHandler());
    if ( kit.getTreeHandler() != null) processor.addHandler( "tree", kit.getTreeHandler());

    // install other handlers
    processor.addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    processor.addHandler( "tooltip", new BindingTagHandler( new TooltipBindingRule()));
    processor.addHandler( "editable", new BindingTagHandler( new EditableBindingRule( TextXidget.allChannel)));
    processor.addHandler( "source", new BindingTagHandler( new TextBindingRule( TextXidget.allChannel)));
    processor.addHandler( "trigger", new TriggerTagHandler());
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
  
  private TagProcessor processor;
}