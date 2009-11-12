/*
 * Xidget - XML Widgets based on JAHM
 * 
 * IComputeNode.java
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

/**
 * An interface for an anchor for one side of a widget.
 */
public interface IComputeNode
{
  /**
   * Returns the identifier of this node (for debugging).
   * @return Returns the identifier of this node (for debugging).
   */
  public int getID();
  
  /**
   * Add the specified dependency.
   * @param node The dependency.
   */
  public void addDependency( IComputeNode node);
  
  /**
   * Remove the specified dependency.
   * @param node The dependency.
   */
  public void removeDependency( IComputeNode node);
  
  /**
   * Clear all dependencies.
   */
  public void clearDependencies();
  
  /**
   * Returns the nodes which must be computed before this node.
   * @return Returns the nodes which must be computed before this node.
   */
  public List<IComputeNode> getDependencies();

  /**
   * Returns true if this node has been assigned a value.
   * @return Returns true if this node has been assigned a value.
   */
  public boolean hasValue();
  
  /**
   * Returns true if this node has a handle for moving it along the x-axis.
   * @return Returns true if this node has a handle.
   */
  public boolean hasXHandle();
  
  /**
   * Returns true if this node has a handle for moving it along the y-axis.
   * @return Returns true if this node has a handle.
   */
  public boolean hasYHandle();
  
  /**
   * Called by the layout algorithm to indicate that a new layout has begun.
   */
  public void reset();
  
  /**
   * Called by the layout algorithm to tell this node to update its value
   * based on the already computed values of its dependencies.
   */
  public void update();

  /**
   * Set the value of this node.
   * @param value The value.
   */
  public void setValue( float value);
  
  /**
   * Returns the value of this node.
   * @return Returns the value of this node.
   */
  public float getValue();
}
