/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.ContainerHeightNode;
import org.xidget.layout.ContainerWidthNode;
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
    this.childNodes = new IComputeNode[ Type.values().length];
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getComputeNode(org.xidget.ifeature.IComputeNodeFeature.Type, boolean)
   */
  public IComputeNode getComputeNode( Type type, boolean container)
  {
    if ( container)
    {
      IWidgetContainerFeature containerFeature = xidget.getFeature( IWidgetContainerFeature.class);
      if ( containerFeature == null) return null;
      
      switch( type)
      {
        case top:
        case left:
          return new ConstantNode( 0); 
          
        case right:
        case width:
          if ( containerWidth == null) containerWidth = new ContainerWidthNode( containerFeature);
          return containerWidth;
          
        case bottom:
        case height: 
          if ( containerHeight == null) containerHeight = new ContainerHeightNode( containerFeature);
          return containerHeight;
      }
    }
    else
    {
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      if ( widgetFeature == null) return null;
      
      int ordinal = type.ordinal();
      IComputeNode node = childNodes[ ordinal];
      if ( node != null) return node;
      
      switch( type)
      {
        case top: node = new WidgetTopNode( widgetFeature); break;
        case left: node = new WidgetLeftNode( widgetFeature); break;
        case right: node = new WidgetRightNode( widgetFeature); break;
        case bottom: node = new WidgetBottomNode( widgetFeature); break;
        case width: node = new WidgetWidthNode( widgetFeature); break;
        case height: node = new WidgetHeightNode( widgetFeature); break;
      }
      
      childNodes[ type.ordinal()] = node;
      return node;
    }
    
    return null;
  }

  protected IXidget xidget;
  protected IComputeNode[] childNodes;
  protected IComputeNode containerWidth;
  protected IComputeNode containerHeight;
}
