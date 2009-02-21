/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import java.util.ArrayList;
import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.util.Fifo;
import org.xmodel.util.HashMultiMap;
import org.xmodel.util.MultiMap;

/**
 * A class which processes an xml fragment using a set of handlers registered by element name.
 */
public class TagProcessor
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
    this.parent = parent;
    this.map = new HashMultiMap<String, ITagHandler>();
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
   * Process the specified fragment.
   * @param root The root of the fragment.
   */
  public void process( IModelObject root) throws TagException
  {
    process( null, root);
  }
  
  /**
   * Process the specified fragment specifying the initial parent handler.
   * @param parent The parent handler of the root.
   * @param root The root of the fragment.
   */
  private void process( ITagHandler parent, IModelObject root) throws TagException
  {
    // process tree in breadth-first order
    Fifo<Entry> fifo = new Fifo<Entry>();
    fifo.push( new Entry( parent, root));
    
    while( !fifo.empty())
    {
      Entry entry = fifo.pop();
      List<ITagHandler> handlers = map.get( entry.element.getType());
      if ( handlers.size() == 1)
      {
        ITagHandler handler = handlers.get( 0);
        if ( handler.filter( this, entry.parent, entry.element))
        {
          if ( handler.process( this, entry.parent, entry.element))
          {
            for( IModelObject child: entry.element.getChildren())
            {
              fifo.add( new Entry( handler, child));
            }
          }
        }
      }
      else if ( handlers.size() > 1)
      {
        List<ITagHandler> list = process( handlers, entry);
        for( IModelObject child: entry.element.getChildren())
        {
          for( ITagHandler handler: list)
          {
            fifo.add( new Entry( handler, child));
          }
        }
      }
    }
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
        if ( handler.process( this, entry.parent, entry.element))
        {
          list.add( handler);
        }
      }
    }
    return list;
  }
    
  /**
   * Add a tag handler. More than one handler can be registered for a particular
   * element name. In this case the handler <code>filter</code> method will be
   * called to disambiguate.
   * @param handler The handler.
   */
  public void addHandler( ITagHandler handler)
  {
    map.put( handler.getTag(), handler);
  }
  
  /**
   * Remove a tag handler.
   * @param handler The handler.
   */
  public void removeHandler( ITagHandler handler)
  {
    map.remove( handler.getTag(), handler);
  }

  /**
   * An entry in the tag processing fifo used to keep track of parentage.
   */
  private class Entry
  {
    public Entry( ITagHandler parent, IModelObject element)
    {
      this.parent = parent;
      this.element = element;
    }
    
    public ITagHandler parent;
    public IModelObject element;
  }
  
  private ITagHandler parent;
  private MultiMap<String, ITagHandler> map;
}
