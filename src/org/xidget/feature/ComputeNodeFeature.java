/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.HashMap;
import java.util.Map;
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
    this.nodes = new HashMap<String, IComputeNode>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IComputeNodeFeature#getAnchor(java.lang.String)
   */
  public IComputeNode getAnchor( String type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    IComputeNode node = nodes.get( type);
    if ( node != null) return node;
    
    if ( type.equals( "x0")) node = new WidgetLeftNode( widget);
    else if ( type.equals( "x1")) node = new WidgetRightNode( widget);
    else if ( type.equals( "y0")) node = new WidgetTopNode( widget);
    else if ( type.equals( "y1")) node = new WidgetBottomNode( widget);
    else if ( type.equals( "xc")) node = new WidgetHorizontalCenterNode( widget);
    else if ( type.equals( "yc")) node = new WidgetVerticalCenterNode( widget);
    else if ( type.equals( "w")) node = new WidgetWidthNode( widget, getAnchor( "x0"), getAnchor( "x1"));
    else if ( type.equals( "h")) node = new WidgetHeightNode( widget, getAnchor( "y0"), getAnchor( "y1"));
    
    nodes.put( type, node);
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getParentAnchor(java.lang.String)
   */
  public IComputeNode getParentAnchor( String type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    IComputeNode node = nodes.get( "p"+type);
    if ( node != null) return node;
    
    Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);    
    if ( type.equals( "x0")) node = new ConstantNode( quad.a);
    else if ( type.equals( "x1")) node = new OffsetNode( new ContainerWidthNode( widget), quad.c);
    else if ( type.equals( "y0")) node = new ConstantNode( quad.b);
    else if ( type.equals( "y1")) node = new OffsetNode( new ContainerHeightNode( widget), quad.d);
    else if ( type.equals( "w")) node = new OffsetNode( new ContainerWidthNode( widget), quad.a + quad.c);
    else if ( type.equals( "h")) node = new OffsetNode( new ContainerHeightNode( widget), quad.b + quad.d);
    
    nodes.put( "p"+type, node);
    return node;    
  }

  private IXidget xidget;
  private Map<String, IComputeNode> nodes;
}
