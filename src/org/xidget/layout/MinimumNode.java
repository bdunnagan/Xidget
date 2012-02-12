/*
 * Xidget - XML Widgets based on JAHM
 * 
 * MaxNode.java
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

/**
 * An implementation of IComputeNode whose value is the maximum value of all of its dependent nodes.
 */
public class MinimumNode extends ComputeNode
{
  /* (non-Javadoc)
   * @see org.xidget.layout.IComputeNode#update()
   */
  public void update()
  {
    if ( !hasValue())
    {
      float min = Float.POSITIVE_INFINITY;
      for( IComputeNode dependency: getDependencies())
      {
        if ( dependency.hasValue())
        {
          if ( min > dependency.getValue())
            min = dependency.getValue();
        }
      }
      
      if ( min != Float.POSITIVE_INFINITY) 
        setValue( min);
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return String.format( "Minimum[%d] = %s <- %s", getID(), printValue(), printDependencies());
  }
}
