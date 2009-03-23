/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.xidget.IFeatures;
import org.xmodel.IModelObject;
import org.xmodel.util.HashMultiMap;
import org.xmodel.util.MultiMap;
import org.xmodel.xpath.expression.IContext;

/**
 * A class which processes an xml fragment using a set of handlers registered by element name.
 */
public class TagProcessor implements IFeatures
{
  /**
   * Create a root tag processor.
   */
  public TagProcessor()
  {
    this( null);
  }
  
  /**
   * Create a tag processor which operates in a subtree of the specified handler.
   * @param parent The parent handler.
   */
  public TagProcessor( ITagHandler parent)
  {
    this.loader = getClass().getClassLoader();
    this.parent = parent;
    this.map = new HashMultiMap<String, ITagHandler>();
    this.roots = new ArrayList<Object>();
  }
  
  /**
   * Set the ClassLoader associated with this tag processor for use by tag handlers
   * which need to dynamic load classes.
   * @param loader The loader.
   */
  public void setClassLoader( ClassLoader loader)
  {
    this.loader = loader;
  }
  
  /**
   * Returns the ClassLoader associated with this processor.
   * @return Returns the ClassLoader associated with this processor.
   */
  public ClassLoader getClassLoader()
  {
    return loader;
  }
  
  /**
   * Returns null or the parent handler.
   * @return Returns null or the parent handler.
   */
  public ITagHandler getParent()
  {
    return parent;
  }
  
  /**
   * Called by root tag handlers.
   * @param object The root object generated by a root tag handler.
   */
  public void addRoot( Object object)
  {
    roots.add( object);
  }

  /**
   * Returns the context with which the process method was invoked.
   * @return Returns the context with which the process method was invoked.
   */
  public IContext getContext()
  {
    return context;
  }
  
  /**
   * Process the specified fragment and return the objects emitted by the root tag handlers.
   * @param context The root of the fragment to be processed.
   * @return Returns the objects emitted by root tag handlers.
   */
  public List<Object> process( IContext context) throws TagException
  {
    roots.clear();
    process( null, context);
    return roots;
  }
  
  /**
   * Process the specified fragment specifying the initial parent handler.
   * @param parent The parent handler of the root.
   * @param context The root of the fragment.
   */
  private void process( ITagHandler parent, IContext context) throws TagException
  {
    this.context = context;
    
    Stack<Entry> stack = new Stack<Entry>();
    stack.push( new Entry( parent, null, context.getObject(), false));
    
    while( !stack.empty())
    {
      Entry entry = stack.pop();
     
      // check for exit entry
      if ( entry.end)
      {
        entry.handler.exit( this, entry.parent, entry.element);
        continue;
      }
      
      // process tag
      List<ITagHandler> handlers = getHandlers( entry.parent, entry.element);
      if ( handlers == null)
      {        
        // push children for unhandled tags
        List<IModelObject> children = entry.element.getChildren();
        for( int i=children.size()-1; i>=0; i--)
        {
          stack.add( new Entry( entry.parent, null, children.get( i), false));
        }        
      }
      else if ( handlers.size() == 1)
      {
        ITagHandler handler = handlers.get( 0);
        if ( handler.filter( this, entry.parent, entry.element))
        {
          if ( handler.enter( this, entry.parent, entry.element))
          {
            List<IModelObject> children = entry.element.getChildren();
            if ( children.size() > 0)
            {
              // push special exit entry
              stack.push( new Entry( entry.parent, handler, entry.element, true));
              
              // push children
              for( int i=children.size()-1; i>=0; i--)
              {
                stack.add( new Entry( handler, null, children.get( i), false));
              }
            }
            else
            {
              handler.exit( this, entry.parent, entry.element);
            }
          }
        }
      }
      else if ( handlers.size() > 1)
      {
        List<ITagHandler> list = process( handlers, entry);
        List<IModelObject> children = entry.element.getChildren();
        for( ITagHandler handler: list)
        {
          if ( children.size() > 0)
          {
            // push special exit entry
            stack.push( new Entry( parent, handler, entry.element, true));
            
            // push children
            for( int i=children.size()-1; i>=0; i--)
            {
              stack.add( new Entry( handler, null, children.get( i), false));
            }
          }
          else
          {
            handler.exit( this, entry.parent, entry.element);
          }
        }
      }
    }
  }
  
  /**
   * Returns the tag handlers for the specified element.
   * @param parent The parent tag handler.
   * @param element The element.
   * @return Returns the tag handlers for the specified element.
   */
  private List<ITagHandler> getHandlers( ITagHandler parent, IModelObject element)
  {
    List<ITagHandler> globals = map.get( null);
    List<ITagHandler> tagged = map.get( element.getType());
    if ( globals == null) return tagged;
    if ( tagged == null) return globals;
    
    List<ITagHandler> handlers = new ArrayList<ITagHandler>();
    handlers.addAll( globals);
    handlers.addAll( map.get( element.getType()));
    return handlers;
  }
  
  /**
   * Process tag with multiple handlers.
   * @param handlers A list containing more than one handler.
   * @param entry The entry to be processed.
   * @return Returns the list of handlers that requested child processing.
   */
  private List<ITagHandler> process( List<ITagHandler> handlers, Entry entry) throws TagException
  {
    List<ITagHandler> list = new ArrayList<ITagHandler>();
    for( int i=handlers.size()-1; i>=0; i--)
    {
      ITagHandler handler = handlers.get( i);
      if ( handler.filter( this, entry.parent, entry.element))
      {
        if ( handler.enter( this, entry.parent, entry.element))
        {
          list.add( handler);
        }
      }
    }
    return list;
  }
  
  /**
   * Add a tag handler which will be tested against every node.
   * @param handler The handler.
   */
  public void addHandler( ITagHandler handler)
  {
    addHandler( null, handler);
  }
  
  /**
   * Remove a tag handler which is tested against every node.
   * @param handler The handler.
   */
  public void removeHandler( ITagHandler handler)
  {
    removeHandler( null, handler);
  }
  
  /**
   * Add a tag handler. More than one handler can be registered for a particular
   * element name. In this case the handler <code>filter</code> method will be
   * called to disambiguate.
   * @param tag The tag.
   * @param handler The handler.
   */
  public void addHandler( String tag, ITagHandler handler)
  {
    map.put( tag, handler);
  }
  
  /**
   * Remove a tag handler.
   * @param tag The tag.
   * @param handler The handler.
   */
  public void removeHandler( String tag, ITagHandler handler)
  {
    map.remove( tag, handler);
  }
  
  /**
   * Returns the handlers for the specified tag.
   * @param tag The tag.
   * @return Returns the handlers for the specified tag.
   */
  public List<ITagHandler> getHandlers( String tag)
  {
    return map.get( tag);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.IFeatures#setFeature(java.lang.Object)
   */
  public void setFeature( Class<? extends Object> featureClass, Object feature)
  {
    if ( features == null) features = new HashMap<Class<? extends Object>, Object>();
    features.put( featureClass, feature);
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  public <T> T getFeature( Class<T> clss)
  {
    if ( features == null) return null;
    return (T)features.get( clss);
  }

  /**
   * An entry in the tag processing fifo used to keep track of parentage.
   */
  private class Entry
  {
    public Entry( ITagHandler parent, ITagHandler handler, IModelObject element, boolean end)
    {
      this.parent = parent;
      this.handler = handler;
      this.element = element;
      this.end = end;
    }
    
    public ITagHandler parent;
    public ITagHandler handler;
    public IModelObject element;
    public boolean end;
  }
 
  private IContext context;
  private ITagHandler parent;
  private ClassLoader loader;
  private MultiMap<String, ITagHandler> map;
  private List<Object> roots;
  private Map<Class<? extends Object>, Object> features;
}