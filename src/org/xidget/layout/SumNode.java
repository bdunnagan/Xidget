/*
 * Xidget - XML Widgets based on JAHM
 * 
 * SumNode.java
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

import java.util.List;
import org.xidget.Log;

/**
 * An implementation of IComputeNode that computes the sum of two other nodes.
 */
public class SumNode extends ComputeNode
{
  /**
   * Compute lhs plus rhs.
   * @param lhs The left-hand-side.
   * @param rhs The right-hand-side.
   */
  public SumNode( IComputeNode lhs, IComputeNode rhs)
  {
    super.addDependency( lhs);
    super.addDependency( rhs);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#getValue()
   */
  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#addDependency(org.xidget.layout.IComputeNode)
   */
  @Override
  public void addDependency( IComputeNode node)
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#clearDependencies()
   */
  @Override
  public void clearDependencies()
  {
    throw new UnsupportedOperationException();
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#update()
   */
  @Override
  public void update()
  {
    if ( !hasValue())
    {
      List<IComputeNode> nodes = getDependencies();
      if ( nodes.get( 0).hasValue() && nodes.get( 1).hasValue())
        setValue( nodes.get( 0).getValue() + nodes.get( 1).getValue());
      
      Log.printf( "layout", "update: %s\n", toString());
    }
  }
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "Sum[%d] = %s <- %s", getID(), printValue(), printDependencies());
  }
}
