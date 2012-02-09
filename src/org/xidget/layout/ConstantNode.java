/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ConstantNode.java
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
 * An IComputeNode with a constant value.
 */
public class ConstantNode extends ComputeNode
{
  public ConstantNode( int value)
  {
    super();
    setValue( value);
  }
  
  public ConstantNode( float value)
  {
    super();
    setValue( value);
  }

  /* (non-Javadoc)
   * @see org.xidget.layout.ComputeNode#reset()
   */
  @Override
  public void reset()
  {
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString()
  {
    return String.format( "Constant[%d] = %s <- %s", getID(), printValue(), printDependencies());
  }
}
