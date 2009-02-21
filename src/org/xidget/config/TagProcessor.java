/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.config;

import java.util.List;
import org.xmodel.BreadthFirstIterator;
import org.xmodel.IModelObject;
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
   * @return Returns false if processing was stopped by a handler.
   */
  public boolean process( IModelObject root)
  {
    BreadthFirstIterator iterator = new BreadthFirstIterator( root);
    while( iterator.hasNext())
    {
      IModelObject element = iterator.next();
      List<ITagHandler> handlers = map.get( element.getType());
      if ( handlers.size() == 1)
      {
        if ( !handlers.get( 0).process( this, element))
          return false;
      }
      else if ( handlers.size() > 1)
      {
        for( ITagHandler handler: handlers)
        {
          if ( handler.filter( this, element))
            if ( !handler.process( this, element))
              return false;
        }
      }
    }
    return true;
  }
  
  /**
   * Add a tag handler. More than one handler can be registered for a particular
   * element name. In this case the handler <code>filter</code> method will be
   * called to disambiguate.
   * @param tag The element name.
   * @param handler The handler.
   */
  public void addHandler( String tag, ITagHandler handler)
  {
    map.put( tag, handler);
  }
  
  /**
   * Remove a tag handler.
   * @param tag The element name.
   * @param handler The handler.
   */
  public void removeHandler( String tag, ITagHandler handler)
  {
    map.remove( tag, handler);
  }

  private ITagHandler parent;
  private MultiMap<String, ITagHandler> map;
}
