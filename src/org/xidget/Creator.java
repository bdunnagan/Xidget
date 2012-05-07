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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.xidget.binding.BackgroundBindingRule;
import org.xidget.binding.BindingTagHandler;
import org.xidget.binding.BoundsBindingRule;
import org.xidget.binding.ChoicesTagHandler;
import org.xidget.binding.ContextTagHandler;
import org.xidget.binding.EditableBindingRule;
import org.xidget.binding.EnableBindingRule;
import org.xidget.binding.FontFamilyBindingRule;
import org.xidget.binding.FontSizeBindingRule;
import org.xidget.binding.FontStyleBindingRule;
import org.xidget.binding.FontTagHandler;
import org.xidget.binding.ForegroundBindingRule;
import org.xidget.binding.GetTagHandler;
import org.xidget.binding.HAlignBindingRule;
import org.xidget.binding.ImageBindingRule;
import org.xidget.binding.ImageBindingRule.Purpose;
import org.xidget.binding.InsideMarginsBindingRule;
import org.xidget.binding.KeyTagHandler;
import org.xidget.binding.LabelBindingRule;
import org.xidget.binding.OutsideMarginsBindingRule;
import org.xidget.binding.PlotsTagHandler;
import org.xidget.binding.ScriptTagHandler;
import org.xidget.binding.SelectionTagHandler;
import org.xidget.binding.SetTagHandler;
import org.xidget.binding.SkipTagHandler;
import org.xidget.binding.SourceTagHandler;
import org.xidget.binding.SpacingBindingRule;
import org.xidget.binding.TitleBindingRule;
import org.xidget.binding.TooltipBindingRule;
import org.xidget.binding.TriggerTagHandler;
import org.xidget.binding.VAlignBindingRule;
import org.xidget.binding.VisibleBindingRule;
import org.xidget.binding.ruler.RulerLabelDepthBindingRule;
import org.xidget.binding.ruler.RulerLabelTagHandler;
import org.xidget.binding.slider.MaximumBindingRule;
import org.xidget.binding.slider.MinimumBindingRule;
import org.xidget.binding.slider.PrecisionBindingRule;
import org.xidget.binding.table.ColumnTitleBindingRule;
import org.xidget.binding.table.RowSetBindingRule;
import org.xidget.binding.table.ShowGridBindingRule;
import org.xidget.binding.table.SubTableTagHandler;
import org.xidget.binding.tree.SubTreeTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IFocusFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.xpath.CapitalizeFunction;
import org.xidget.xpath.DateAddFunction;
import org.xidget.xpath.DateFormatFunction;
import org.xidget.xpath.DateNowFunction;
import org.xidget.xpath.DateParseFunction;
import org.xidget.xpath.DateSetFunction;
import org.xidget.xpath.FileExistsFunction;
import org.xidget.xpath.FontsFunction;
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
    this.map = new HashMap<Object, IXidget>();
    this.roots = new ArrayList<IXidget>( 1);

    // general
    processor.addHandler( "bcolor", new BindingTagHandler( new BackgroundBindingRule()));
    processor.addHandler( "bounds", new BindingTagHandler( new BoundsBindingRule()));
    processor.addHandler( "choices", new ChoicesTagHandler());
    processor.addHandler( "context", new ContextTagHandler());
    processor.addHandler( "editable", new BindingTagHandler( new EditableBindingRule()));
    processor.addHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    processor.addHandler( "font", new FontTagHandler());
    processor.addHandler( "fcolor", new BindingTagHandler( new ForegroundBindingRule()));
    processor.addHandler( "get", new GetTagHandler());
    processor.addHandler( "image", new BindingTagHandler( new ImageBindingRule( Purpose.up)));
    processor.addHandler( "imageUp", new BindingTagHandler( new ImageBindingRule( Purpose.up)));
    processor.addHandler( "imageDown", new BindingTagHandler( new ImageBindingRule( Purpose.down)));
    processor.addHandler( "imageHover", new BindingTagHandler( new ImageBindingRule( Purpose.hover)));
    processor.addHandler( "label", new BindingTagHandler( new LabelBindingRule()));
    processor.addHandler( "margins", new BindingTagHandler( new InsideMarginsBindingRule()));
    processor.addHandler( "insideMargins", new BindingTagHandler( new InsideMarginsBindingRule()));
    processor.addHandler( "outsideMargins", new BindingTagHandler( new OutsideMarginsBindingRule()));
    processor.addHandler( "plots", new PlotsTagHandler());
    processor.addHandler( "rows", new BindingTagHandler( new RowSetBindingRule()));
    processor.addHandler( "selection", new SelectionTagHandler());
    processor.addHandler( "set", new SetTagHandler());
    processor.addHandler( "source", new SourceTagHandler());
    processor.addHandler( "spacing", new BindingTagHandler( new SpacingBindingRule()));
    processor.addHandler( "title", new BindingTagHandler( new TitleBindingRule()));
    processor.addHandler( "title", new BindingTagHandler( new ColumnTitleBindingRule(), true));
    processor.addHandler( "tooltip", new BindingTagHandler( new TooltipBindingRule()));
    processor.addHandler( "trigger", new TriggerTagHandler());
    processor.addHandler( "visible", new BindingTagHandler( new VisibleBindingRule()));
    processor.addHandler( "halign", new BindingTagHandler( new HAlignBindingRule()));
    processor.addHandler( "valign", new BindingTagHandler( new VAlignBindingRule()));

    // ruler
    processor.addHandler( "label", new RulerLabelTagHandler());
    processor.addHandler( "depth", new BindingTagHandler( new RulerLabelDepthBindingRule()));
    processor.addAttributeHandler( "label", new RulerLabelTagHandler());
    processor.addAttributeHandler( "depth", new BindingTagHandler( new RulerLabelDepthBindingRule()));
    
    // slider
    processor.addHandler( "min", new BindingTagHandler( new MinimumBindingRule()));
    processor.addHandler( "minimum", new BindingTagHandler( new MinimumBindingRule()));
    processor.addHandler( "max", new BindingTagHandler( new MaximumBindingRule()));
    processor.addHandler( "maximum", new BindingTagHandler( new MaximumBindingRule()));
    processor.addHandler( "precision", new BindingTagHandler( new PrecisionBindingRule()));
    processor.addAttributeHandler( "min", new BindingTagHandler( new MinimumBindingRule()));
    processor.addAttributeHandler( "minimum", new BindingTagHandler( new MinimumBindingRule()));
    processor.addAttributeHandler( "max", new BindingTagHandler( new MaximumBindingRule()));
    processor.addAttributeHandler( "maximum", new BindingTagHandler( new MaximumBindingRule()));
    processor.addAttributeHandler( "precision", new BindingTagHandler( new PrecisionBindingRule()));
    
    // tables
    processor.addHandler( "showGrid", new BindingTagHandler( new ShowGridBindingRule()));
    
    // attributes
    processor.addAttributeHandler( "bcolor", new BindingTagHandler( new BackgroundBindingRule()));
    processor.addAttributeHandler( "bounds", new BindingTagHandler( new BoundsBindingRule()));
    processor.addAttributeHandler( "context", new ContextTagHandler());
    processor.addAttributeHandler( "editable", new BindingTagHandler( new EditableBindingRule()));
    processor.addAttributeHandler( "enable", new BindingTagHandler( new EnableBindingRule()));
    processor.addAttributeHandler( "fcolor", new BindingTagHandler( new ForegroundBindingRule()));
    processor.addAttributeHandler( "font", new FontTagHandler());
    processor.addAttributeHandler( "family", new BindingTagHandler( new FontFamilyBindingRule( "font")));
    processor.addAttributeHandler( "style", new BindingTagHandler( new FontStyleBindingRule( "font")));
    processor.addAttributeHandler( "size", new BindingTagHandler( new FontSizeBindingRule( "font")));
    processor.addAttributeHandler( "image", new BindingTagHandler( new ImageBindingRule( Purpose.up)));
    processor.addAttributeHandler( "imageUp", new BindingTagHandler( new ImageBindingRule( Purpose.up)));
    processor.addAttributeHandler( "imageDown", new BindingTagHandler( new ImageBindingRule( Purpose.down)));
    processor.addAttributeHandler( "imageHover", new BindingTagHandler( new ImageBindingRule( Purpose.hover)));
    processor.addAttributeHandler( "label", new BindingTagHandler( new LabelBindingRule()));
    processor.addAttributeHandler( "label", new RulerLabelTagHandler());
    processor.addAttributeHandler( "margins", new BindingTagHandler( new InsideMarginsBindingRule()));
    processor.addAttributeHandler( "insideMargins", new BindingTagHandler( new InsideMarginsBindingRule()));
    processor.addAttributeHandler( "outsideMargins", new BindingTagHandler( new OutsideMarginsBindingRule()));
    processor.addAttributeHandler( "source", new SourceTagHandler());
    processor.addAttributeHandler( "spacing", new BindingTagHandler( new SpacingBindingRule()));
    processor.addAttributeHandler( "title", new BindingTagHandler( new TitleBindingRule()));
    processor.addAttributeHandler( "title", new BindingTagHandler( new ColumnTitleBindingRule(), true));
    processor.addAttributeHandler( "halign", new BindingTagHandler( new HAlignBindingRule()));
    processor.addAttributeHandler( "valign", new BindingTagHandler( new VAlignBindingRule()));
    processor.addAttributeHandler( "visible", new BindingTagHandler( new VisibleBindingRule()));
    
    // skip
    processor.addHandler( "functions", new SkipTagHandler());
    
    // scripts
    processor.addHandler( "onPress", new ScriptTagHandler());
    processor.addHandler( "onOpen", new ScriptTagHandler());
    processor.addHandler( "onClose", new ScriptTagHandler());
    processor.addHandler( "onShow", new ScriptTagHandler());
    processor.addHandler( "onHide", new ScriptTagHandler());
    processor.addHandler( "onDrag", new ScriptTagHandler());
    processor.addHandler( "onDrop", new ScriptTagHandler());
    processor.addHandler( "onClick", new ScriptTagHandler());
    processor.addHandler( "onDoubleClick", new ScriptTagHandler());
    processor.addHandler( "onFocusGain", new ScriptTagHandler());
    processor.addHandler( "onFocusLost", new ScriptTagHandler());
    processor.addHandler( "onDisplay", new ScriptTagHandler());
    processor.addHandler( "onSet", new ScriptTagHandler());
    processor.addHandler( "onGet", new ScriptTagHandler());
    processor.addHandler( "onSelect", new ScriptTagHandler());
    processor.addHandler( "onDeselect", new ScriptTagHandler());
    
    // key bindings
    processor.addHandler( "onKey", new KeyTagHandler());

    // sub-tables and sub-trees
    processor.addHandler( "tree", new SubTreeTagHandler());
    processor.addHandler( "table", new SubTableTagHandler());
    
    // register functions
    registerCustomXPaths();
  }

  /**
   * Set the toolkit class.
   * @param toolkitClass The toolkit class.
   */
  public static void setToolkitClass( Class<? extends IToolkit> toolkitClass)
  {
    Creator.toolkitClass = toolkitClass;
  }

  /**
   * Returns the platofmr-specific tookit.
   * @return Returns the platofmr-specific tookit.
   */
  public static IToolkit getToolkit()
  {
    Creator creator = getInstance();
    if ( creator.toolkit == null) 
    {
      try
      {
        creator.toolkit = toolkitClass.newInstance();
        creator.toolkit.configure( creator.processor);
      }
      catch( Exception e)
      {
        throw new RuntimeException( e);
      }
    }
    return creator.toolkit;
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
    FunctionFactory.getInstance().register( FontsFunction.name, FontsFunction.class);
    FunctionFactory.getInstance().register( IsFolderFunction.name, IsFolderFunction.class);
    FunctionFactory.getInstance().register( ValidateXPathFunction.name, ValidateXPathFunction.class);
    FunctionFactory.getInstance().register( DateFormatFunction.name, DateFormatFunction.class);
    FunctionFactory.getInstance().register( DateParseFunction.name, DateParseFunction.class);
    FunctionFactory.getInstance().register( DateAddFunction.name, DateAddFunction.class);
    FunctionFactory.getInstance().register( DateSetFunction.name, DateSetFunction.class);
    FunctionFactory.getInstance().register( DateNowFunction.name, DateNowFunction.class);
  }
  
  /**
   * Register an instance of IXidget with its associated widget.  This method is not normally called
   * by clients.  Instances of IXidget are automatically registered with the widgets created by their
   * IWidgetCreationFeature implementation.
   * @param widget The widget.
   * @param xidget The xidget.
   */
  public void register( Object widget, IXidget xidget)
  {
    map.put( widget, xidget);
    
    //
    // This behavior was added to support JAppletXidget, which is the only Swing xidget that
    // is not built by the Creator class.  In general, any xidget being registered with this
    // method will already be parented, except the root xidget.
    //
    if ( xidget.getParent() == null && !roots.contains( xidget)) roots.add( xidget);
  }
  
  /**
   * Returns the xidget that created the specified widget.
   * @param widget The widget.
   * @return Returns null or the xidget.
   */
  public IXidget getXidget( Object widget)
  {
    return map.get( widget);
  }
  
  /**
   * @return Returns the list of xidgets that have been created without parents.
   */
  public List<IXidget> getActiveHierarchies()
  {
    return roots;
  }
  
  /**
   * Search the xidget hierarchies for the first xidget that was constructed from the specified configuration element.
   * The search begins with the hierarchy containing the xidget that has focus.  If a matching xidget is not found
   * then the each of the root hierarchies are searched in an unspecified order until a match is found.
   * @param config The configuration element.
   * @return Returns null or the first matching xidget.
   */
  public IXidget findXidget( IModelObject config)
  {
    List<IXidget> roots = getActiveHierarchies();
    
    IXidget focus = getToolkit().getFeature( IFocusFeature.class).getFocus();
    if ( focus != null)
    {
      IXidget parent = focus.getParent();
      while( parent != null) { focus = parent; parent = parent.getParent();}
      roots.remove( focus);
      roots.add( 0, focus);
    }
    
    for( IXidget root: roots)
    {
      IXidget xidget = findXidget( root, config);
      if ( xidget != null) return xidget;
    }
    
    return null;
  }
  
  /**
   * Search the xidget hierarchies for the xidgets that were constructed from the specified configuration element.
   * Each active hierarchy is searched and all matching xidgets are returned.
   * @param config The configuration element.
   * @return Returns the list of matching xidgets.
   */
  public List<IXidget> findXidgets( IModelObject config)
  {
    List<IXidget> matches = new ArrayList<IXidget>();
    
    for( IXidget root: getActiveHierarchies())
    {
      IXidget xidget = findXidget( root, config);
      if ( xidget != null) matches.add( xidget);
    }
    
    return matches;
  }
  
  /**
   * Search the descendants of the specified xidget for the xidget constructed from the specified configuration element.
   * Note that it is possible for multiple xidgets to be constructed from the same configuration element.  However, it
   * is necessary to clone a configuration element for each new xidget if this is the case.  Also, it is the 
   * responsibility of the application to keep track of these cloned configuration elements because the framework
   * assumes that xidgets can be found from their configuration elements with the only additional piece of information
   * being the xidget that currently has focus.
   * @param root The root of the xidget hierarchy to search.
   * @param config The configuration element.
   * @return Returns null or the matching xidget.
   */
  private IXidget findXidget( IXidget root, IModelObject config)
  {
    if ( config == null) return null;
    
    Stack<IXidget> stack = new Stack<IXidget>();
    stack.push( root);
    while( !stack.empty())
    {
      IXidget current = stack.pop();
      
      if ( current.getConfig().equals( config)) return current;
      
      for( IXidget child: current.getChildren()) 
      {
        if ( child != current)
          stack.push( child);
      }
    }
    return null;
  }
  
  /**
   * Create the xidget hierarchy for the specified configuration.
   * @param parent Null or the parent xidget.
   * @param configContext The configuration context.
   */
  public List<IXidget> create( IXidget parent, int index, StatefulContext configContext) throws TagException
  {
    // parse configuration
    List<IXidget> xidgets = (parent != null)? parse( parent, index, configContext): parse( configContext);
    if ( xidgets.size() == 0) return Collections.emptyList();
    
    // build
    for( IXidget xidget: xidgets) build( xidget);
    
    return xidgets;
  }
  
  /**
   * Create and bind the xidget hierarchy for the specified configuration.
   * @param parent Null or the parent xidget.
   * @param index The index of insertion.
   * @param configContext The configuration context.
   * @param bindContext The binding context.
   */
  public List<IXidget> create( IXidget parent, int index, StatefulContext configContext, StatefulContext bindContext) throws TagException
  {
    List<IXidget> xidgets = create( parent, index, configContext);
    
    for( IXidget xidget: xidgets)
    {
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.bind( (StatefulContext)bindContext);
    }
    
    return xidgets;
  }
      
  /**
   * Unbind and dispose the specified xidget hierarchy.
   * @param xidget The xidget.
   */
  public void destroy( IXidget root)
  {
    // unbind all contexts
    IBindFeature bindFeature = root.getFeature( IBindFeature.class);
    StatefulContext[] contexts = bindFeature.getBoundContexts().toArray( new StatefulContext[ 0]);
    for( StatefulContext context: contexts) bindFeature.unbind( (StatefulContext)context);
    
    // remove root
    IWidgetCreationFeature creationFeature = root.getFeature( IWidgetCreationFeature.class);
    Object[] widgets = creationFeature.getLastWidgets();
    for( Object widget: widgets) map.remove( widget);
    roots.remove( root);
    
    // remove xidget from parent
    IXidget parent = root.getParent();
    if ( parent != null) parent.getChildren().remove( root);
    
    // destroy widget hierarchy
    creationFeature.destroyWidgets( parent);
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
      if ( feature != null) 
      {
        feature.createWidgets();
        for( Object widget: feature.getLastWidgets())
          register( widget, xidget);
      }
      
      // push children in reverse order
      List<IXidget> children = xidget.getChildren();
      for( int i = children.size() - 1; i >= 0; i--)
        stack.push( children.get( i));
    }
  }
  
  /**
   * Destroy and recreate the specified xidget.
   * @param xidget The xidget.
   * @return Returns null or the new xidget.
   */
  public IXidget rebuild( IXidget xidget) throws TagException
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context == null) return null;

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
    xidget = findXidget( config);
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
   * @param parent Null or a parent xidget.
   * @param index The index of insertion.
   * @param element The root of the configuration.
   * @return Returns the list of xidgets created.
   */
  public List<IXidget> parse( IXidget parent, int index, StatefulContext context) throws TagException
  {
    List<IXidget> children = new ArrayList<IXidget>( parent.getChildren());
    if ( index == -1) index = children.size();
    processor.process( new ParentTagHandler( parent, index), context);

    List<IXidget> created = new ArrayList<IXidget>( parent.getChildren());
    created.removeAll( children);
    return created;
  }
  
  /**
   * Returns the singleton.
   * @return Returns the singleton.
   */
  public static Creator getInstance()
  {
    Creator instance = instances.get();
    if ( instance == null)
    {
      instance = new Creator();
      instances.set( instance);
    }
    return instance;
  }

  /**
   * An implementation of ITagHandler that is passed to the TagProcessor to support parenting 
   * of newly created xidgets when rebuilding a sub-tree of the xidget hierarchy.
   */
  public class ParentTagHandler implements ITagHandler, IXidgetFeature
  {
    public ParentTagHandler( IXidget xidget)
    {
      this( xidget, xidget.getChildren().size());
    }
    
    public ParentTagHandler( IXidget xidget, int index)
    {
      this.xidget = xidget;
      this.index = index;
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
    
    /**
     * @return Returns the index of insertion.
     */
    public int getIndex()
    {
      return index;
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
    private int index;
  }
  
  private static ThreadLocal<Creator> instances = new ThreadLocal<Creator>();
  private static Class<? extends IToolkit> toolkitClass;
  private IToolkit toolkit;
  private TagProcessor processor;
  private Map<Object, IXidget> map;
  private List<IXidget> roots;
}
