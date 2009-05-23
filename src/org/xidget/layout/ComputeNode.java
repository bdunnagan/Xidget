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
    for( IComputeNode dependency: getDependencies())
    {
      if ( dependency.hasValue())
      {
        hasValue = true;
        setValue( dependency.getValue());
        break;
      }
    }
    
    if ( hasValue()) Log.printf( "layout", "update: (%.1f) %s\n", getValue(), toString()); 
    else Log.printf( "layout", "update: (?) %s\n", toString());
  }
  
  /**
   * Print the list of dependencies.
   * @return Returns a string containing the printed dependencies.
   */
  protected String printDependencies()
  {
    if ( dependencies == null || dependencies.size() == 0) return "";
    
    StringBuilder sb = new StringBuilder();
    sb.append( " {");
    String sep = " ";
    for( IComputeNode dependency: dependencies)
    {
      sb.append( sep); sep = ", ";
      sb.append( dependency);
    }
    sb.append( "}");
    
    return sb.toString();
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
