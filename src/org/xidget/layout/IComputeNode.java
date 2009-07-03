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
   * Called by the layout algorithm to tell this node to update its value
   * based on the already computed values of its dependencies.
   */
  public void update();

  /**
   * Returns the value of this node.
   * @return Returns the value of this node.
   */
  public float getValue();
  
  /**
   * Sets the value of this node.
   * @param value The value.
   */
  public void setValue( float value);
  
  public enum Grab { none, x, y};
  
  /**
   * Called when a mouse grab event (usually left-click drag) is received by the container.
   * @param x The x-coordinate of the event.
   * @param y The y-coordinate of the event.
   * @return Returns true if the node is grabbed.
   */
  public Grab mouseGrab( int x, int y);
  
  /**
   * When a node is successfully grabbed, the container will send move events.
   * @param px The percentage of the container width of the mouse position.
   * @param py The percentage of the container height of the mouse position.
   */
  public void move( float px, float py);
}
