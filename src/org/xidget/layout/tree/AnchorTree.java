/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.xidget.layout.Attachment;
import org.xidget.layout.IAnchor;
import org.xidget.layout.V2D;

/**
 * An anchor tree structure for computing layout.
 * Note that this object actually represents multiple trees.
 */
public final class AnchorTree
{
  /**
   * Create an anchor tree to be built later by calling the <code>build</code> method.
   */
  public AnchorTree()
  {
    computed = new HashSet<AnchorNode>();
  }
  
  /**
   * Create an anchor tree from the specified attachments.
   * @param attachments The attachments.
   */
  public AnchorTree( List<Attachment> attachments)
  {
    computed = new HashSet<AnchorNode>();
    build( attachments);
  }
  
  /**
   * Build the tree from the specified set of attachments.
   * @param attachments The attachments.
   */
  public void build( List<Attachment> attachments)
  {
    map = new HashMap<IAnchor, AnchorNode>();
    for( Attachment attachment: attachments)
    {
      IAnchor[] anchors = attachment.anchors;
      AnchorNode node = map.get( anchors[ 0]);
      if ( node == null)
      {
        node = new AnchorNode();
        map.put( node.anchor, node);
      }
      
      node.anchor = anchors[ 0];
      node.children = new ArrayList<AnchorNode>();
      
      AnchorNode child = new AnchorNode();
      child.anchor = anchors[ 1];
      child.distance = attachment.distance;
      child.parent = node;
      map.put( child.anchor, child);
    }
    
    // find root node
    roots = new HashSet<AnchorNode>();
    for( AnchorNode node: map.values())
    {
      roots.add( getRoot( node));
    }
  }
  
  /**
   * Returns the root of the specified node.
   * @param node The node.
   * @return Returns the root.
   */
  private AnchorNode getRoot( AnchorNode node)
  {
    AnchorNode parent = node.parent;
    while( parent != null) 
    {
      node = parent;
      parent = node.parent;
    }
    return node; 
  }
  
  /**
   * Compute the layout of the entire tree.
   */
  public void compute()
  {
    computed.clear();
    for( AnchorNode root: roots)
      computeDown( root);
    computed.clear();
  }
  
  /**
   * Recompute the layout from the specified anchor.
   * @param anchor The anchor.
   */
  public void anchorMoved( IAnchor anchor)
  {
    computed.clear();
    AnchorNode start = map.get( anchor);
    computeUp( start);
    computeDown( start);
    computed.clear();
  }
  
  /**
   * Recompute the layout of ancestors of the specified node.
   * @param start The start node.
   */
  private void computeUp( AnchorNode start)
  {
    AnchorNode node1 = start;
    AnchorNode node2 = start.parent;
    while( node2 != null)
    { 
      computed.add( node1);
      if ( computeBackward( node1.anchor, node2.anchor, node1.distance))
      {
        computeDown( node2);
        node1 = node2;
        node2 = node2.parent;
      }
      else
      {
        break;
      }
    }
  }
  
  /**
   * Recompute the descendants of the specified node.
   */
  private void computeDown( AnchorNode start)
  {
    Stack<AnchorNode> stack = new Stack<AnchorNode>();
    stack.push( start);
    while( !stack.empty())
    {
      AnchorNode node = stack.pop();
      computed.add( node);
      for( AnchorNode child: node.children)
      {
        if ( computeForward( node.anchor, child.anchor, child.distance))
        {
          stack.push( child);
        }
      }
    }
  }
  
  /**
   * Compute v2 = v1 + d.
   * @param anchor1 The first anchor.
   * @param anchor2 The second anchor.
   * @param distance The distance between the anchors.
   * @return Returns true if the position of anchor2 was changed.
   */
  private boolean computeForward( IAnchor anchor1, IAnchor anchor2, V2D distance)
  {
    V2D v = anchor1.getPoint();
    return anchor2.moveTo( v.x + distance.x, v.y + distance.y);
  }
  
  /**
   * Compute v1 = v2 - d.
   * @param anchor1 The first anchor.
   * @param anchor2 The second anchor.
   * @param distance The distance between the anchors.
   * @return Returns true if the position of anchor2 was changed.
   */
  private boolean computeBackward( IAnchor anchor1, IAnchor anchor2, V2D distance)
  {
    V2D v = anchor1.getPoint();
    return anchor2.moveTo( v.x - distance.x, v.y - distance.y);
  }
  
  private Map<IAnchor, AnchorNode> map;
  private Set<AnchorNode> roots;
  private Set<AnchorNode> computed;
}
