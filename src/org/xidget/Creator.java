/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.xidget.binding.BindingTagHandler;
import org.xidget.binding.ChoicesTagHandler;
import org.xidget.binding.EnableBindingRule;
import org.xidget.binding.LayoutTagHandler;
import org.xidget.binding.TooltipBindingRule;
import org.xidget.binding.TriggerTagHandler;
import org.xidget.binding.table.ColumnTitleBindingRule;
import org.xidget.binding.table.RowSetBindingRule;
import org.xidget.binding.text.EditableBindingRule;
import org.xidget.binding.text.TextBindingRule;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A convenience class that provides methods for parsing a configuration file, 
 * creating the widget hierarchy and binding xidgets.  This class is built with
 * an instance of TagProcessor which has already been configured with platform
 * specific tag handlers.
 */
public final class Creator
{
  protected Creator()
  {
    this.processor = new TagProcessor();

    processor.addHandler( "choices", new ChoicesTagHandler());
    processor.addHandler( "column", new BindingTagHandler( new ColumnTitleBindingRule()));
    processor.addHandler( "editable", new BindingTagHandler( new EditableBindingRule()));
    processor.addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    processor.addHandler( "rows", new BindingTagHandler( new RowSetBindingRule()));
    processor.addHandler( "source", new BindingTagHandler( new TextBindingRule()));
    processor.addHandler( "tooltip", new BindingTagHandler( new TooltipBindingRule()));
    processor.addHandler( "trigger", new TriggerTagHandler());

    processor.addHandler( "layout", new LayoutTagHandler());
    processor.addAttributeHandler( "layout", new LayoutTagHandler());
    
    // processor.addHandler( "context", new BindingTagHandler( new ContextBindingRule()));
    
    // toolkit configuration
    if ( toolkit == null) throw new RuntimeException( "Platform toolkit not defined on Creator.");
    toolkit.configure( processor);
  }

  /**
   * Set the toolkit used by the creator singleton. This method may not work
   * if the toolkit is specified from a thread other than the UI thread.
   * @param toolkit The toolkit.
   */
  public static void setToolkit( IToolkit toolkit)
  {
    Creator.toolkit = toolkit;
  }
  
  /**
   * Parse the specified xidget configuration. (Features are configured here)
   * @param element The root of the configuration.
   * @return Returns the list of xidgets created.
   */
  public List<IXidget> parse( StatefulContext context) throws TagException
  {
    List<Object> result = processor.process( context);
    List<IXidget> xidgets = new ArrayList<IXidget>( result.size());
    for( Object object: result) xidgets.add( (IXidget)object);
    return xidgets;
  }
  
  /**
   * Create the widget hierarchy for the specified xidget. The root of the widget
   * hierarchy can be accessed through an appropriate platform-specific feature.
   * @param root The root of the xidget hierarchy.
   */
  public void build( IXidget root)
  {
    Stack<IXidget> stack = new Stack<IXidget>();
    stack.push( root);
    while( !stack.empty())
    {
      IXidget xidget = stack.pop();
      
      IWidgetCreationFeature feature = xidget.getFeature( IWidgetCreationFeature.class);
      if ( feature != null) feature.createWidget();
      
      for( IXidget child: xidget.getChildren())
        stack.push( child);
    }
  }

  /**
   * Returns the singleton.
   * @return Returns the singleton.
   */
  public static Creator getInstance()
  {
    if ( instance == null) instance = new Creator();
    return instance;
  }
  
  private static Creator instance;
  private static IToolkit toolkit;
  private TagProcessor processor;
}
