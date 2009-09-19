/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.util.HashMap;
import java.util.Map;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A thread-safe map that allows specific instances of StatefulContext to be associated 
 * and accessed by name. The map uses thread-local data to provide one map per thread.
 */
public class NamedContexts
{
  protected NamedContexts()
  {
  }

  /**
   * Returns the instance of StatefulContext associated with the specified name.
   * @param name The name of the context.
   * @return Returns null or an instance of StatefulContext.
   */
  public static StatefulContext get( String name)
  {
    Map<String, StatefulContext> map = getMap();
    return map.get( name);
  }
  
  /**
   * Associate the specified context with the specified name.
   * @param name The name of the context.
   * @param context The context.
   */
  public static void set( String name, StatefulContext context)
  {
    Map<String, StatefulContext> map = getMap();
    map.put( name, context);
  }
  
  /**
   * Remove the specified named context form the map.
   * @param name The name of the context.
   */
  public static void remove( String name)
  {
    Map<String, StatefulContext> map = getMap();
    map.remove( name);
  }
  
  /**
   * Returns the instance of NamedContextMap for this thread.
   * @return Returns an instance of NamedContextMap.
   */
  private static Map<String, StatefulContext> getMap()
  {
    Map<String, StatefulContext> map = threadMap.get();
    if ( map == null)
    {
      map = new HashMap<String, StatefulContext>();
      threadMap.set( map);
    }
    return map;
  }
  
  private static ThreadLocal<Map<String, StatefulContext>> threadMap = new ThreadLocal<Map<String, StatefulContext>>(); 
}
