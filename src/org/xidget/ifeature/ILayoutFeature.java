/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.ifeature;

import java.util.List;
import org.xidget.config.TagProcessor;
import org.xidget.layout.IComputeNode;
import org.xmodel.IModelObject;

/**
 * An interface for the layout algorithm feature. This feature is responsible
 * for computing new positions for widgets as a result of a change in the
 * position of a particular widget. This feature is associated with xidgets
 * that represent containers.
 */
public interface ILayoutFeature
{
  /**
   * Layout the children of the container.
   */
  public void layout();
  
  /**
   * Configure the layout.
   * @param processor The tag processor.
   * @param element The layout element.
   */
  public void configure( TagProcessor processor, IModelObject element);
  
  /**
   * Reconfigure the layout from the current configuration element.
   */
  public void reconfigure();
  
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
   * Returns the list of nodes.
   * @return Returns the list of nodes.
   */
  public List<IComputeNode> getNodes();
  
  /**
   * Reset the layout and remove cached information.
   */
  public void reset();
}
