/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xidget.layout.IComputeNode;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the layout algorithm feature. This feature is responsible
 * for computing new positions for widgets as a result of a change in the
 * position of a particular widget. This feature is associated with xidgets
 * that represent containers.
 */
public interface ILayoutFeature
{
  /**
   * Configure the layout from the layout script or attachment declarations.
   * This method will reevaluate the configuration when called more than once.
   */
  public void configure();
  
  /**
   * Layout the children of the container.
   * @param context The widget context.
   */
  public void layout( StatefulContext context);
  
  /**
   * Add a node to the layout.
   * @param node The computation node.
   */
  public void addNode( IComputeNode node);
  
  /**
   * Remove a node from the layout.
   * @param node The node to be removed.
   */
  public void removeNode( IComputeNode node);

  /**
   * Remove all nodes from the layout.
   */
  public void clearNodes();

  /**
   * Returns the list of nodes.
   * @return Returns the list of nodes.
   */
  public List<IComputeNode> getNodes();
}
