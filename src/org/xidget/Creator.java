/*
 * Xidget - XML Widgets based on JAHM
 * 
 * Creator.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import org.xidget.binding.BackgroundBindingRule;
import org.xidget.binding.BindingTagHandler;
import org.xidget.binding.ChildrenTagHandler;
import org.xidget.binding.ChoicesTagHandler;
import org.xidget.binding.ContextTagHandler;
import org.xidget.binding.EnableBindingRule;
import org.xidget.binding.ErrorBindingRule;
import org.xidget.binding.FontBindingRule;
import org.xidget.binding.FontSizeBindingRule;
import org.xidget.binding.FontStyleBindingRule;
import org.xidget.binding.ForegroundBindingRule;
import org.xidget.binding.IconBindingRule;
import org.xidget.binding.LabelBindingRule;
import org.xidget.binding.ScriptTagHandler;
import org.xidget.binding.SelectionTagHandler;
import org.xidget.binding.SkipTagHandler;
import org.xidget.binding.SourceTagHandler;
import org.xidget.binding.TitleBindingRule;
import org.xidget.binding.TooltipBindingRule;
import org.xidget.binding.TriggerTagHandler;
import org.xidget.binding.VisibleBindingRule;
import org.xidget.binding.table.ColumnTitleBindingRule;
import org.xidget.binding.table.RowSetBindingRule;
import org.xidget.binding.table.ShowGridBindingRule;
import org.xidget.binding.table.SubTableTagHandler;
import org.xidget.binding.text.EditableBindingRule;
import org.xidget.binding.text.XinTagHandler;
import org.xidget.binding.text.XoutTagHandler;
import org.xidget.binding.tree.SubTreeTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.xpath.CapitalizeFunction;
import org.xidget.xpath.FileExistsFunction;
import org.xidget.xpath.IsFolderFunction;
import org.xidget.xpath.ValidateXPathFunction;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.function.FunctionFactory;

/**
 * A convenience class that provides methods for parsing a configuration file, 
 * creating the widget hierarchy and binding xidgets.  This class is built with
 * an instance of TagProcessor which has already been configured with platform
 * specific tag handlers.
 * <p>
 * Xidget processing has three primary stages: xidget creation, widget creation
 * and binding.  Xidgets are created when a configuration fragment is parsed 
 * with an appropriately configured TagProcessor.  The complete xidget hierarchy
 * represented in the fragment is created.  The corresponding widget hierarchy
 * is created next using the <code>build</code> method.  Finally, the xidget
 * hierarchy is bound by visiting each IBindFeature.
 */
public final class Creator
{
  protected Creator()
  {
    this.processor = new TagProcessor();

    // general
    processor.addHandler( "background", new BindingTagHandler( new BackgroundBindingRule()));
    processor.addHandler( "children", new ChildrenTagHandler());
    processor.addHandler( "choices", new ChoicesTagHandler());
    processor.addHandler( "context", new ContextTagHandler());
    processor.addHandler( "column", new BindingTagHandler( new ColumnTitleBindingRule(), true));
    processor.addHandler( "editable", new BindingTagHandler( new EditableBindingRule()));
    processor.addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    processor.addHandler( "error", new BindingTagHandler( new ErrorBindingRule()));
    processor.addHandler( "font", new BindingTagHandler( new FontBindingRule()));
    processor.addHandler( "fontStyle", new BindingTagHandler( new FontStyleBindingRule()));
    processor.addHandler( "fontSize", new BindingTagHandler( new FontSizeBindingRule()));
    processor.addHandler( "foreground", new BindingTagHandler( new ForegroundBindingRule()));
    processor.addHandler( "image", new BindingTagHandler( new IconBindingRule()));
    processor.addHandler( "label", new BindingTagHandler( new LabelBindingRule()));
    processor.addHandler( "rows", new BindingTagHandler( new RowSetBindingRule()));
    processor.addHandler( "selection", new SelectionTagHandler());
    processor.addHandler( "source", new SourceTagHandler());
    processor.addHandler( "title", new BindingTagHandler( new TitleBindingRule()));
    processor.addHandler( "tooltip", new BindingTagHandler( new TooltipBindingRule()));
    processor.addHandler( "trigger", new TriggerTagHandler());
    processor.addHandler( "visible", new BindingTagHandler( new VisibleBindingRule()));
    processor.addHandler( "xin", new XinTagHandler());
    processor.addHandler( "xout", new XoutTagHandler());
    
    // tables
    processor.addHandler( "showGrid", new BindingTagHandler( new ShowGridBindingRule()));
    
    // attributes
    processor.addAttributeHandler( "background", new BindingTagHandler( new BackgroundBindingRule()));
    processor.addAttributeHandler( "children", new ChildrenTagHandler());
    processor.addAttributeHandler( "context", new ContextTagHandler());
    processor.addAttributeHandler( "foreground", new BindingTagHandler( new ForegroundBindingRule()));
    processor.addAttributeHandler( "font", new BindingTagHandler( new FontBindingRule()));
    processor.addAttributeHandler( "fontStyle", new BindingTagHandler( new FontStyleBindingRule()));
    processor.addAttributeHandler( "fontSize", new BindingTagHandler( new FontSizeBindingRule()));
    processor.addAttributeHandler( "image", new BindingTagHandler( new IconBindingRule()));
    processor.addAttributeHandler( "label", new BindingTagHandler( new LabelBindingRule()));
    processor.addAttributeHandler( "title", new BindingTagHandler( new TitleBindingRule()));
    
    // skip
    processor.addHandler( "functions", new SkipTagHandler());
    
    // scripts
    processor.addHandler( "onPress", new ScriptTagHandler());
    processor.addHandler( "onOpen", new ScriptTagHandler());
    processor.addHandler( "onClose", new ScriptTagHandler());
    processor.addHandler( "onDrag", new ScriptTagHandler());
    processor.addHandler( "onDrop", new ScriptTagHandler());

    // sub-tables and sub-trees
    processor.addHandler( "tree", new SubTreeTagHandler());
    processor.addHandler( "table", new SubTableTagHandler());
    
    // register functions
    registerCustomXPaths();
  }

  /**
   * Set the toolkit used by the creator singleton. This method may not work
   * if the toolkit is specified from a thread other than the UI thread.
   * @param toolkit The toolkit.
   */
  public void setToolkit( IToolkit toolkit)
  {
    this.toolkit = toolkit; 
    if ( toolkit != null) toolkit.configure( processor);
  }

  /**
   * Returns the platofmr-specific tookit.
   * @return Returns the platofmr-specific tookit.
   */
  public IToolkit getToolkit()
  {
    return toolkit;
  }
  
  /**
   * Returns the TagProcessor. Clients are free to change the handlers.
   * @return Returns the TagProcessor.
   */
  public TagProcessor getTagProcessor()
  {
    return processor;
  }
  
  /**
   * Register custom xpaths.
   */
  private void registerCustomXPaths()
  {
    FunctionFactory.getInstance().register( CapitalizeFunction.name, CapitalizeFunction.class);
    FunctionFactory.getInstance().register( FileExistsFunction.name, FileExistsFunction.class);
    FunctionFactory.getInstance().register( IsFolderFunction.name, IsFolderFunction.class);
    FunctionFactory.getInstance().register( ValidateXPathFunction.name, ValidateXPathFunction.class);
  }
  
  /**
   * Create the xidget hierarchy for the specified configuration.
   * @param parent Null or the parent xidget.
   * @param configContext The configuration context.
   */
  public List<IXidget> create( IXidget parent, StatefulContext configContext) throws TagException
  {
    long t0 = System.nanoTime();
    
    // parse configuration
    List<IXidget> xidgets = (parent != null)? parse( parent, configContext): parse( configContext);
    if ( xidgets.size() == 0) return Collections.emptyList();
    
    long t1 = System.nanoTime();
    Log.printf( "perf", "parse: %3.2fms\n", (t1-t0)/1000000f);
    
    for( IXidget xidget: xidgets)
    {
      // build widget hierarchy
      build( xidget);
      
      long t2 = System.nanoTime();
      Log.printf( "perf", "build: %3.2fms\n", (t2-t1)/1000000f);
      t1 = t2;
    }
    
    return xidgets;
  }
  
  /**
   * Create and bind the xidget hierarchy for the specified configuration.
   * @param parent Null or the parent xidget.
   * @param configContext The configuration context.
   * @param bindContext The binding context.
   */
  public List<IXidget> create( IXidget parent, StatefulContext configContext, StatefulContext bindContext) throws TagException
  {
    long t0 = System.nanoTime();
    List<IXidget> xidgets = create( parent, configContext);
    for( IXidget xidget: xidgets)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.bind( (StatefulContext)bindContext);
    
      long t1 = System.nanoTime();
      Log.printf( "perf", "bind: %3.2fms\n", (t1-t0)/1000000f);
    }
    return xidgets;
  }
      
  /**
   * Unbind and dispose the specified xidget hierarchy.
   * @param xidget The xidget.
   */
  public void destroy( IXidget xidget)
  {
    // unbind all contexts
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext[] contexts = bindFeature.getBoundContexts().toArray( new StatefulContext[ 0]);
    for( StatefulContext context: contexts) bindFeature.unbind( (StatefulContext)context);
    
    // destroy widget hierarchy
    IWidgetCreationFeature creationFeature = xidget.getFeature( IWidgetCreationFeature.class);
    creationFeature.destroyWidgets();
    
    // clear xidget attribure
    xidget.getConfig().removeAttribute( "instance");
    
    // remove xidget from parent
    if ( xidget.getParent() != null) 
      xidget.getParent().getChildren().remove( xidget);
  }
  
  /**
   * Destroy and recreate the specified xidget.
   * @param xidget The xidget.
   * @return Returns null or the new xidget.
   */
  public IXidget rebuild( IXidget xidget) throws TagException
  {
    // get context
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    List<StatefulContext> contexts = bindFeature.getBoundContexts();
    if ( contexts.size() == 0) return null;

    // only rebind first context
    StatefulContext context = contexts.get( 0);

    // reset parent layout
    IXidget parent = xidget.getParent();
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    if ( layoutFeature != null) layoutFeature.invalidate();
    
    // destroy
    destroy( xidget);
    
    //
    // Reparse the configuration. If the parse fails, save the exception to rethrow
    // after the xidget has been rebuilt. Otherwise, the context that was unbound
    // will be forgotten and the xidget cannot be rebuilt.
    //
    IModelObject config = xidget.getConfig();
    TagException exception = null;
    try
    {
      processor.process( new ParentTagHandler( xidget.getParent()), new StatefulContext( context, config));
    }
    catch( TagException e)
    {
      exception = e;
    }
    
    // create widget hierarchy
    xidget = (IXidget)config.getAttribute( "instance");
    build( xidget);
    
    // bind the xidget
    bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.bind( (StatefulContext)context);
    
    // rethrow parse exception
    if ( exception != null) throw exception;
    
    return xidget;
  }
  
  /**
   * Parse the specified xidget configuration.
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
   * Parse the specified xidget configuration.
   * @param xidget Null or a parent xidget.
   * @param element The root of the configuration.
   * @return Returns the list of xidgets created.
   */
  public List<IXidget> parse( IXidget parent, StatefulContext context) throws TagException
  {
    List<IXidget> children = new ArrayList<IXidget>( parent.getChildren());
    processor.process( new ParentTagHandler( parent), context);

    List<IXidget> created = new ArrayList<IXidget>( parent.getChildren());
    created.removeAll( children);
    return created;
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
      if ( feature != null) feature.createWidgets();
      
      // push children in reverse order
      List<IXidget> children = xidget.getChildren();
      for( int i = children.size() - 1; i >= 0; i--)
        stack.push( children.get( i));
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

  /**
   * An implementation of ITagHandler that is passed to the TagProcessor to support parenting 
   * of newly created xidgets when rebuilding a sub-tree of the xidget hierarchy.
   */
  private class ParentTagHandler implements ITagHandler, IXidgetFeature
  {
    public ParentTagHandler( IXidget xidget)
    {
      this.xidget = xidget;
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
      return false;
    }

    /* (non-Javadoc)
     * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
     */
    public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
    {
    }

    /* (non-Javadoc)
     * @see org.xidget.config.ifeature.IXidgetFeature#getXidget()
     */
    public IXidget getXidget()
    {
      return xidget;
    }

    /* (non-Javadoc)
     * @see org.xidget.IFeatured#getFeature(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T> T getFeature( Class<T> clss)
    {
      if ( clss == IXidgetFeature.class) return (T)this;
      return null;
    }
    
    private IXidget xidget;
  }
  
  private static Creator instance;
  private IToolkit toolkit;
  private TagProcessor processor;
}
