/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.List;

/**
 * An interface for an anchor for one side of a widget.
 */
public interface IComputeNode
{
  /**
   * Add the specified dependency.
   * @param node The dependency.
   */
  public void addDependency( IComputeNode node);
  
  /**
   * Returns the nodes which must be computed before this node.
   * @return Returns the nodes which must be computed before this node.
   */
  public List<IComputeNode> getDependencies();
  
  /**
   * Called by the layout algorithm to tell this node to update its value
   * based on the already computed values of its dependencies.
   */
  public void update();

  /**
   * Returns true if the node has a defined value.
   * @return Returns true if the node has a defined value.
   */
  public boolean hasValue();
  
  /**
   * Returns the value of this node.
   * @return Returns the value of this node.
   */
  public int getValue();
  
  /**
   * Sets the value of this node.
   * @param value The value.
   */
  public void setValue( int value);
}
