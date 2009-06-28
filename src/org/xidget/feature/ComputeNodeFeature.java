/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.WidgetBottomNode;
import org.xidget.layout.WidgetHeightNode;
import org.xidget.layout.WidgetLeftNode;
import org.xidget.layout.WidgetRightNode;
import org.xidget.layout.WidgetTopNode;
import org.xidget.layout.WidgetWidthNode;

/**
 * An implementation of IComputeNodeFeature.
 */
public class ComputeNodeFeature implements IComputeNodeFeature
{
  public ComputeNodeFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.nodes = new IComputeNode[ Type.values().length];
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getAnchor(org.xidget.ifeature.IComputeNodeFeature.Type)
   */
  public IComputeNode getComputeNode( Type type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    int ordinal = type.ordinal();
    IComputeNode node = nodes[ ordinal];
    if ( node != null) return node;
    
    switch( type)
    {
      case top: node = new WidgetTopNode( widget); break;
      case left: node = new WidgetLeftNode( widget); break;
      case right: node = new WidgetRightNode( widget); break;
      case bottom: node = new WidgetBottomNode( widget); break;
      case width: node = new WidgetWidthNode( widget); break;
      case height: node = new WidgetHeightNode( widget); break;
    }

    nodes[ type.ordinal()] = node;
    return node;
  }

  protected IXidget xidget;
  protected IComputeNode[] nodes;
}
