/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import java.util.ArrayList;
import java.util.List;
import org.xidget.BindingTagHandler;
import org.xidget.ContextBindingRule;
import org.xidget.EnableBindingRule;
import org.xidget.IXidget;
import org.xidget.TooltipBindingRule;
import org.xidget.TriggerTagHandler;
import org.xidget.config.processor.TagException;
import org.xidget.config.processor.TagProcessor;
import org.xidget.text.EditableBindingRule;
import org.xidget.text.TextBindingRule;
import org.xmodel.xpath.expression.IContext;

/**
 * The reference configuration parser which uses an IXidgetKit to obtain xidgets
 * for the appropriate widget platform, and installs all other platform-independent
 * tag handlers required to parse a xidget configuration file.
 */
public class Configuration
{
  /**
   * Create a configuration parser for the currently defined toolkit.
   */
  public Configuration()
  {
    // install xidget kit
    processor = new TagProcessor();
    
    if ( kit.getFormHandler() != null) processor.addHandler( "application", kit.getApplicationHandler());
    if ( kit.getFormHandler() != null) processor.addHandler( "dialog", kit.getDialogHandler());
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
    processor.addHandler( "editable", new BindingTagHandler( new EditableBindingRule()));
    processor.addHandler( "source", new BindingTagHandler( new TextBindingRule()));
    processor.addHandler( "context", new BindingTagHandler( new ContextBindingRule()));
    processor.addHandler( "trigger", new TriggerTagHandler());
  }
  
  /**
   * Set the xidget toolkit. This method must be called before instantiating this class.
   * @param kit The toolkit.
   */
  public static void setToolkit( IXidgetKit kit)
  {
    Configuration.kit = kit;
  }
  
  /**
   * Parse the specified root of a xidget configuration.
   * @param root The root element.
   */
  public List<IXidget> parse( IContext context) throws TagException
  {
    List<Object> objects = processor.process( context);
    List<IXidget> xidgets = new ArrayList<IXidget>( objects.size());
    for( Object object: objects) xidgets.add( (IXidget)object);
    return xidgets;
  }
  
  private static IXidgetKit kit;
  private TagProcessor processor;
}