/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ILayoutFeature.java
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
package org.xidget.ifeature;

import java.util.List;

import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An interface for the layout algorithm feature. This feature is responsible for computing new
 * positions for widgets as a result of a change in the position of a particular widget. This
 * feature is associated with xidgets that represent containers.
 */
public interface ILayoutFeature
{
  public enum Side { top, left, right, bottom};
  
  /**
   * Discard cached layout information.
   */
  public void invalidate();
  
  /**
   * Layout the children of the container.
   * @param context The widget context.
   */
  public void layout( StatefulContext context);
    
  /**
   * Attach the specified xidget to its previous peer.
   * @param xidget The xidget.
   * @param fromSide The side of the xidget to be attached.
   * @param toSide The side of the previous xidget.
   * @param offset The offset.
   */
  public void attachPrevious( IXidget xidget, Side fromSide, Side toSide, int offset);
  
  /**
   * Attach the specified xidget to its next peer.
   * @param xidget The xidget.
   * @param fromSide The side of the xidget to be attached.
   * @param toSide The side of the next xidget.
   * @param offset The offset.
   */
  public void attachNext( IXidget xidget, Side fromSide, Side toSide, int offset);
  
  /**
   * Attach the specified xidget to the opposite side of the specific peer.
   * @param xidget The xidget.
   * @param side The side of the xidget to be attached.
   * @param peer The peer xidget.
   * @param offset The offset.
   */
  public void attachPeer( IXidget xidget, Side side, IXidget peer, int offset);
  
  /**
   * Attach the specified xidget to a specific peer.
   * @param xidget The xidget.
   * @param fromSide The side of the xidget to be attached.
   * @param peer The peer xidget.
   * @param toSide The side of the previous xidget.
   * @param offset The offset.
   */
  public void attachPeer( IXidget xidget, Side fromSide, IXidget peer, Side toSide, int offset);

  /**
   * Attach the specified xidget to its container.
   * @param xidget The xidget.
   * @param side The side of the xidget to be attached.
   * @param offset The offset from the container.
   */
  public void attachContainer( IXidget xidget, Side side, int offset);

  /**
   * Attach the specified xidget to its container at a position calculated as a percentage
   * of the appropriate dimension of the container. For example, if the top-side is being
   * attached, then the height of the container is used.
   * @param xidget The xidget.
   * @param side The side of the xidget to be attached.
   * @param percent The percentage of the dimension of the container.
   * @param percentNode The node which will hold changes to the percentage when handle is moved.
   * @param offset The offset from the container.
   * @param handle True if the anchor point can be moved.
   */
  public void attachContainer( IXidget xidget, Side side, float percent, IModelObject percentNode, int offset, boolean handle);
  
  /**
   * Attach the right or bottom side of the container to all of the specified xidgets. The container
   * is attached using a special node that calculates the maximum extent of the xidgets.
   * @param xidget The xidget.
   * @param side The side to which the container will be attached.
   * @param offset The offset.
   */
  public void packContainer( List<IXidget> xidgets, Side side, int offset);
}
