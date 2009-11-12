/*
 * Xidget - XML Widgets based on JAHM
 * 
 * NamedContexts.java
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
