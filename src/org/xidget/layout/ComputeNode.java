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
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    for( IComputeNode dependency: getDependencies())
    {
      if ( dependency.hasValue())
      {
        int value = dependency.getValue();
//        System.out.printf( "%s: %d\n", toString(), value);
        setValue( value);
        break;
      }
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( getClass().getSimpleName());
    sb.append( "#");
    sb.append( hashCode());
    
    sb.append( " {");
    for( IComputeNode dependency: getDependencies())
    {
      sb.append( ", ");
      sb.append( dependency);
    }
    sb.append( "}");
    
    return sb.toString();
  }

  private List<IComputeNode> dependencies;
}
