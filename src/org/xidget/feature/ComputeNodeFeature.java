/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.IXidget;
import org.xidget.config.util.Quad;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.ContainerHeightNode;
import org.xidget.layout.ContainerWidthNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.WidgetBottomNode;
import org.xidget.layout.WidgetHeightNode;
import org.xidget.layout.WidgetHorizontalCenterNode;
import org.xidget.layout.WidgetLeftNode;
import org.xidget.layout.WidgetRightNode;
import org.xidget.layout.WidgetTopNode;
import org.xidget.layout.WidgetVerticalCenterNode;
import org.xidget.layout.WidgetWidthNode;
import org.xmodel.Xlate;

/**
 * An implementation of IComputeNodeFeature.
 */
public class ComputeNodeFeature implements IComputeNodeFeature
{
  public ComputeNodeFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.nodes = new IComputeNode[ Type.values().length];
    this.parentNodes = new IComputeNode[ Type.values().length];
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getAnchor(org.xidget.ifeature.IComputeNodeFeature.Type)
   */
  public IComputeNode getAnchor( Type type)
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
      case vertical_center: node = new WidgetVerticalCenterNode( widget); break;
      case horizontal_center: node = new WidgetHorizontalCenterNode( widget); break;
    }

    nodes[ type.ordinal()] = node;
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getParentAnchor(org.xidget.ifeature.IComputeNodeFeature.Type)
   */
  public IComputeNode getParentAnchor( Type type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    int ordinal = type.ordinal();
    IComputeNode node = parentNodes[ ordinal];
    if ( node != null) return node;
    
    Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
    switch( type)
    {
      case top: node = new ConstantNode( quad.b); break;
      case left: node = new ConstantNode( quad.a); break;
      case right: node = new OffsetNode( new ContainerWidthNode( widget), -quad.c); break;
      case bottom: node = new OffsetNode( new ContainerHeightNode( widget), -quad.d); break;
      case width: node = new OffsetNode( new ContainerWidthNode( widget), -(quad.a + quad.c)); break;
      case height: node = new OffsetNode( new ContainerHeightNode( widget), -(quad.b + quad.d)); break;
    }
    
    parentNodes[ ordinal] = node;
    return node;    
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#clearParentAnchors()
   */
  public void clearParentAnchors()
  {
    nodes = new IComputeNode[ Type.values().length];
    parentNodes = new IComputeNode[ Type.values().length];
  }

  protected IXidget xidget;
  protected IComputeNode[] nodes;
  protected IComputeNode[] parentNodes;
}
