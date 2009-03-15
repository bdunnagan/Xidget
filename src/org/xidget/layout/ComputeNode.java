/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#addDependency(org.xidget.layout.IComputeNode)
   */
  public void addDependency( IComputeNode node)
  {
    if ( dependencies == null) dependencies = new ArrayList<IComputeNode>( 1);
    dependencies.add( 0, node);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getDepends()
   */
  public List<IComputeNode> getDependencies()
  {
    return (dependencies == null)? Collections.<IComputeNode>emptyList(): dependencies;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#hasValue()
   */
  public boolean hasValue()
  {
    return hasValue;
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    hasValue = false;
    System.out.print( toString());    
    for( IComputeNode dependency: getDependencies())
    {
      if ( dependency.hasValue())
      {
        hasValue = true;
        int value = dependency.getValue();
        System.out.printf( " <- %s (=%d)", dependency, value);
        setValue( value);
        break;
      }
      else
      {
        System.out.printf( " <- %s (=?)", dependency);
      }
    }
    System.out.println( " "+hasValue());
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return getClass().getSimpleName();
  }

  private List<IComputeNode> dependencies;
  private boolean hasValue;
}
