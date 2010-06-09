/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that maps key sequences to an arbitrary type. Key sequences are defined by a string of
 * the form: [mod[+mod[+...]],]key1,key2,... That is, any number of modifiers separated by the plus
 * (+) sign followed by a comma and then one or more primary keys. This class does not place any
 * restriction on what keys can be modifier keys. The significance of a modifier key is that all
 * permutations of the set of modifiers are bound.
 */
public class KeyTree<T>
{
  public KeyTree()
  {
    root = new TreeNode<T>();
    node = root;
  }
  
  /**
   * Returns true if the specified key sequence is valid.
   * @param keys The key sequence string.
   * @return Returns true if the specified key sequence is valid.
   */
  public boolean validKeySequence( String keys)
  {
    Matcher matcher = regex.matcher( keys);
    return matcher.matches();
  }
  
  /**
   * Add a binding to the tree.
   * @param keys The key configuration.
   * @param binding The object to bind.
   */
  public void bind( String keys, T binding)
  {
    Matcher matcher = regex.matcher( keys);
    if ( matcher.matches())
    {
      String g1 = matcher.group( 1);
      String[] modifiers = null;
      if ( g1 != null)
      {
        g1 = g1.substring( 0, g1.length() - 1);
        modifiers = g1.split( "\\s*[+]\\s*");
      }
      else
      {
        modifiers = new String[ 0];
      }
      
      String g5 = matcher.group( 5);
      String[] primaries = g5.split( "\\s*,\\s*");
      
      bind( modifiers, primaries, binding);
    }
  }
  
  /**
   * Remove a binding from the tree.
   * @param keys The key configuration.
   */
  public void unbind( String keys)
  {
    Matcher matcher = regex.matcher( keys);
    if ( matcher.matches())
    {
      String g1 = matcher.group( 1);
      String[] modifiers = null;
      if ( g1 != null)
      {
        g1 = g1.substring( 0, g1.length() - 1);
        modifiers = g1.split( "\\s*[+]\\s*");
      }
      else
      {
        modifiers = new String[ 0];
      }
      
      String g5 = matcher.group( 5);
      String[] primaries = g5.split( "\\s*,\\s*");
      
      unbind( modifiers, primaries);
    }
  }
  
  /**
   * Bind the specified key sequence(s).
   * @param modifiers The modifiers.
   * @param primaries The primaries.
   * @param binding The binding.
   */
  private void bind( String[] modifiers, String[] primaries, T binding)
  {
    if ( modifiers.length < 2)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      bind( sequence, binding);
    }
    else if ( modifiers.length == 2)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      bind( sequence, binding);
      
      Collections.swap( sequence, 0, 1);
      bind( sequence, binding);
    }
    else if ( modifiers.length == 3)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      
      for( int i=0; i<3; i++)
      {
        bind( sequence, binding);
        Collections.swap( sequence, 1, 2);
        bind( sequence, binding);
        Collections.swap( sequence, 0, 2);
      }
    }
  }
  
  /**
   * Unbind the specified key sequence(s).
   * @param modifiers The modifiers.
   * @param primaries The primaries.
   */
  private void unbind( String[] modifiers, String[] primaries)
  {
    if ( modifiers.length < 2)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      unbind( sequence);
    }
    else if ( modifiers.length == 2)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      unbind( sequence);
      
      Collections.swap( sequence, 0, 1);
      unbind( sequence);
    }
    else if ( modifiers.length == 3)
    {
      List<String> sequence = new ArrayList<String>();
      sequence.addAll( Arrays.asList( modifiers));
      sequence.addAll( Arrays.asList( primaries));
      
      for( int i=0; i<3; i++)
      {
        unbind( sequence);
        Collections.swap( sequence, 1, 2);
        unbind( sequence);
        Collections.swap( sequence, 0, 2);
      }
    }
  }
  
  /**
   * Bind the specified key sequence.
   * @param sequence The key sequence.
   * @param binding The binding.
   */
  private void bind( List<String> sequence, T binding)
  {
    System.out.print( "sequence:");
    TreeNode<T> iter = root;
    for( String key: sequence)
    {
      System.out.printf(  " %s", key);
      
      if ( iter.children == null)
        iter.children = new HashMap<String, TreeNode<T>>();
      
      TreeNode<T> next = iter.children.get( key);
      if ( next == null)
      {
        next = new TreeNode<T>();
        next.parent = iter;
        next.key = key;
        iter.children.put( key, next);  
      }
      
      iter = next;
    }
    
    iter.binding = binding;
    System.out.println( "");
  }
  
  /**
   * Unbind the specified key sequence.
   * @param sequence The key sequence.
   */
  private void unbind( List<String> sequence)
  {
    TreeNode<T> iter = root;
    for( String key: sequence)
    {
      if ( iter.children == null) return;
      
      TreeNode<T> next = iter.children.get( key);
      if ( next == null) return;
      
      iter = next;
    }

    // clear binding
    iter.binding = null;
    
    // discard unused nodes
    while( iter.parent != null && iter.children == null || iter.children.size() == 0)
    {
      iter.parent.children.remove( iter.key);
    }
  }
  
  /**
   * Reset the key-tracking state.
   */
  public void reset()
  {
    System.out.println( "KeyTree.reset()");
    node = root;
  }
  
  /**
   * Tell the map that the specified key was pressed.
   * @param key The key.
   * @return Returns null or the binding.
   */
  public T keyDown( String key)
  {
    System.out.println( "KeyTree.keyDown(): "+key);
    
    TreeNode<T> next = null;
    if ( node.children != null)
    {
      next = node.children.get( key);
      if ( next != null) node = next;
    }
    
    if ( next != null)
    {
      T binding = next.binding;
      return binding;
    }
    else
    {
      TreeNode<T> dummy = new TreeNode<T>();
      dummy.key = key;
      dummy.parent = node;
      node = dummy;
    }
    
    return null;
  }
  
  /**
   * Tell the map that the specified key was released.
   * @param key The key.
   */
  public void keyUp( String key)
  {
    System.out.println( "KeyTree.keyUp(): "+key);
    
    // search sequence for key
    TreeNode<T> iter = node;
    while( iter != root && !iter.key.equals( key))
    {
      iter = iter.parent;
    }

    if ( iter != root) node = iter.parent; else node = root;
  }
  
  class TreeNode<U>
  {
    private String key;
    private TreeNode<U> parent;
    public Map<String, TreeNode<U>> children;
    public U binding;
  }
    
  private final static String modifiers = 
    "shift|control|option|command|alt|meta";
  
  private final static Pattern regex = Pattern.compile( String.format( 
    "((%s)([+](%s))*\\s*[,])?\\s*(([^+, ]+)(\\s*[,]\\s*[^+, ]+)*)", modifiers, modifiers));
  
  private TreeNode<T> root;
  private TreeNode<T> node;
}