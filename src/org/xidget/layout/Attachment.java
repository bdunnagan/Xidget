/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

/**
 * An attachment between two xidgets.
 */
public class Attachment
{
  /**
   * Create an attachment between the two anchors with zero distance.
   * @param anchor1 The first anchor.
   * @param anchor2 The second anchor.
   */
  public Attachment( IAnchor anchor1, IAnchor anchor2)
  {
    this( anchor1, anchor2, new V2D());
  }
  
  /**
   * Create an attachment between the two anchors with the specified offset. 
   * When calculating the position of anchor2 relative to anchor1, the vector
   * equation, anchor2 = anchor1 + distance, is used.  When calculating the 
   * position of anchor1 relative to anchor2, the vector equation,
   * anchor1 = anchor2 - distance, is used.
   * @param anchor1 The first anchor.
   * @param anchor2 The second anchor.
   * @param distance The distance between the two anchors.
   */
  public Attachment( IAnchor anchor1, IAnchor anchor2, V2D distance)
  {
    anchors = new IAnchor[ 2];
    anchors[ 0] = anchor1;
    anchors[ 1] = anchor2;
    this.distance = distance;
  }
  
  public IAnchor[] anchors;
  public V2D distance;
}
