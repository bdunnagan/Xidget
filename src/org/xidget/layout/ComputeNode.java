/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.Log;

/**
 * An abstract base class for anchors.
 */
public abstract class ComputeNode implements IComputeNode
{
  public ComputeNode()
  {
    id = ++sid;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getID()
   */
  public int getID()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#addDependency(org.xidget.layout.IComputeNode)
   */
  public void addDependency( IComputeNode node)
  {
    if ( dependencies == null) dependencies = new ArrayList<IComputeNode>( 1);
    if ( !dependencies.contains( node)) dependencies.add( 0, node);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#removeDependency(org.xidget.layout.IComputeNode)
   */
  public void removeDependency( IComputeNode node)
  {
    if ( dependencies != null) dependencies.remove( node);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#clearDependencies()
   */
  public void clearDependencies()
  {
    if ( dependencies != null) dependencies.clear();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getDepends()
   */
  public List<IComputeNode> getDependencies()
  {
    return (dependencies == null)? Collections.<IComputeNode>emptyList(): dependencies;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasXHandle()
   */
  public boolean hasXHandle()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasYHandle()
   */
  public boolean hasYHandle()
  {
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    return hasValue || hasDefault;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#reset()
   */
  public void reset()
  {
    hasValue = false;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    if ( !hasValue)
    {
      for( IComputeNode dependency: getDependencies())
      {
        if ( dependency.hasValue())
        {
          setValue( dependency.getValue());
          break;
        }
      }
      
      Log.printf( "layout", "update: %s\n", toString()); 
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  public float getValue()
  {
    return value;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setValue(float)
   */
  public void setValue( float value)
  {
    this.value = value;
    this.hasValue = true;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#setDefaultValue(java.lang.Float)
   */
  public void setDefaultValue( Float value)
  {
    if ( value == null) 
    {
      hasDefault = false;
    }
    else
    {
      hasDefault = true;
      setValue( value);
    }
  }

  /**
   * Print the value of the node or ? if not defined.
   * @return Returns the value string.
   */
  protected String printValue()
  {
    return hasValue()? String.format( "%2.1f", getValue()): "?";
  }
  
  /**
   * Print the list of dependencies.
   * @return Returns a string containing the printed dependencies.
   */
  protected String printDependencies()
  {
    if ( dependencies == null || dependencies.size() == 0) return "";
    
    if ( cycle) return " ...";
    cycle = true;
    
    StringBuilder sb = new StringBuilder();
    for( IComputeNode dependency: dependencies)
    {
      if ( sb.length() > 0) sb.append( ", ");
      sb.append( dependency.getID());
    }
    
    cycle = false;
    return sb.toString();
  }

  private static int sid = 1000;

  private List<IComputeNode> dependencies;
  private boolean hasValue;
  private boolean hasDefault;
  private float value;
  private int id;
  private boolean cycle;
}
