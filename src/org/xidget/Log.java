/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * A simple, not particularly efficient logging class.
 */
public class Log
{
  public Log()
  {
    debugs = new HashSet<String>();
    
    Properties properties = System.getProperties();
    for( Object key: properties.keySet())
    {
      String name = key.toString();
      if ( name.startsWith( "log."))
        debugs.add( name.substring( 4));
    }
  }
  
  /**
   * Log for the specified system.
   * @param system The system.
   * @param format The format string.
   * @param objects The arguments.
   */
  public void logf( String system, String format, Object...objects)
  {
    if ( debugs.contains( system))
    {
      String string = String.format( format, objects);
      String[] lines = string.split( "\n");
      for( String line: lines)
      {
        System.out.printf( "[%s] %s\n", system.toUpperCase(), line);
      }
    }
  }
  
  /**
   * Log for the specified system.
   * @param system The system.
   * @param message The message.
   */
  public void log( String system, String message)
  {
    if ( debugs.contains( system))
    {
      String[] lines = message.split( "\n");
      for( String line: lines)
      {
        System.out.printf( "[%s] %s\n", system.toUpperCase(), line);
      }
    }
  }
  
  /**
   * Log an exception from the specified system.
   * @param system The system.
   * @param e The exception.
   */
  public void log( String system, Exception e)
  {
    StringWriter string = new StringWriter();
    PrintWriter writer = new PrintWriter( string);
    e.printStackTrace( writer);
    System.err.println( string.toString());
  }
  
  /**
   * Log for the specified system.
   * @param system The system.
   * @param format The format string.
   * @param objects The arguments.
   */
  public static void printf( String system, String format, Object...objects)
  {
    getInstance().logf( system, format, objects);
  }

  /**
   * Log for the specified system.
   * @param system The system.
   * @param message The message.
   */
  public static void println( String system, String message)
  {
    getInstance().log( system, message);
  }
  
  /**
   * Log an exception for the specified system.
   * @param system The system.
   * @param e The exception.
   */
  public static void exception( String system, Exception e)
  {
    Log.getInstance().log( system, e);
  }
  
  /**
   * Returns the singleton.
   * @return Returns the singleton.
   */
  public static Log getInstance()
  {
    if ( instance == null) instance = new Log();
    return instance;
  }
  
  private static Log instance;
  private Set<String> debugs;
}
