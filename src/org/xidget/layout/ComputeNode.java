/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ComputeNode.java
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
package org.xidget.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    return hasValue;
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
    for( IComputeNode dependency: getDependencies())
    {
      if ( dependency.hasValue())
      {
        setValue( dependency.getValue());
        break;
      }
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
    sb.append( "{");
    for( IComputeNode dependency: dependencies)
    {
      if ( sb.length() > 0) sb.append( ", ");
      sb.append( dependency.getID());
    }
    sb.append( "}");
    
    cycle = false;
    return sb.toString();
  }

  private static int sid = 0;

  private List<IComputeNode> dependencies;
  private boolean hasValue;
  private float value;
  private int id;
  private boolean cycle;
}
