/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.impl;

import org.xidget.IXidget;
import org.xidget.feature.IComputeNodeFeature;
import org.xidget.feature.IWidgetFeature;
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
    this.nodes = new IComputeNode[ 6];
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.IComputeNodeFeature#getAnchor(java.lang.String)
   */
  public IComputeNode getAnchor( String type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    char c0 = type.charAt( 0);
    if ( c0 == 'x')
    {
      char c1 = type.charAt( 1);
      if ( c1 == '0')
      {
        if ( nodes[ 0] == null) nodes[ 0] = new WidgetLeftNode( widget);
        return nodes[ 0];
      }
      else
      {
        if ( nodes[ 2] == null) nodes[ 2] = new WidgetRightNode( widget);
        return nodes[ 2];
      }
    }
    else if ( c0 == 'y')
    {
      char c1 = type.charAt( 1);
      if ( c1 == '0')
      {
        if ( nodes[ 1] == null) nodes[ 1] = new WidgetTopNode( widget);
        return nodes[ 1];
      }
      else
      {
        if ( nodes[ 3] == null) nodes[ 3] = new WidgetBottomNode( widget);
        return nodes[ 3];
      }
    }
    else if ( c0 == 'w')
    {
      if ( nodes[ 4] == null) nodes[ 4] = new WidgetWidthNode( widget, getAnchor( "x0"), getAnchor( "x1"));
      return nodes[ 4];
    }
    else 
    {
      if ( nodes[ 5] == null) nodes[ 5] = new WidgetHeightNode( widget, getAnchor( "x0"), getAnchor( "x1"));
      return nodes[ 5];
    }    
  }

  private IXidget xidget;
  private IComputeNode[] nodes;
}
