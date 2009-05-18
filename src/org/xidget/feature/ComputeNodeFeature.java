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
import org.xidget.layout.WidgetLeftNode;
import org.xidget.layout.WidgetRightNode;
import org.xidget.layout.WidgetTopNode;
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
    this.nodes = new IComputeNode[ 10];
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
      if ( nodes[ 5] == null) nodes[ 5] = new WidgetHeightNode( widget, getAnchor( "y0"), getAnchor( "y1"));
      return nodes[ 5];
    }    
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getParentAnchor(java.lang.String)
   */
  public IComputeNode getParentAnchor( String type)
  {
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    if ( widget == null) return null;
    
    char c0 = type.charAt( 0);
    if ( c0 == 'x')
    {
      char c1 = type.charAt( 1);
      if ( c1 == '0')
      {
        if ( nodes[ 6] == null) 
        {
          Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
          nodes[ 6] = new ConstantNode( quad.a);
        }
        return nodes[ 6];
      }
      else
      {
        if ( nodes[ 7] == null) 
        {
          Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
          nodes[ 7] = new OffsetNode( new ContainerWidthNode( widget), quad.c);
        }
        return nodes[ 7];
      }
    }
    else if ( c0 == 'y')
    {
      char c1 = type.charAt( 1);
      if ( c1 == '0')
      {
        if ( nodes[ 8] == null) 
        {
          Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
          nodes[ 8] = new ConstantNode( quad.b);
        }
        return nodes[ 8];
      }
      else
      {
        if ( nodes[ 9] == null) 
        {
          Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
          nodes[ 9] = new OffsetNode( new ContainerHeightNode( widget), quad.d);
        }
        return nodes[ 9];
      }
    }
    else if ( c0 == 'w')
    {
      if ( nodes[ 7] == null) 
      {
        Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
        nodes[ 7] = new OffsetNode( new ContainerWidthNode( widget), quad.a + quad.c);
      }
      return nodes[ 7];
    }
    else 
    {
      if ( nodes[ 9] == null) 
      {
        Quad quad = new Quad( Xlate.get( xidget.getConfig(), "margins", (String)null), 0, 0, 0, 0);
        nodes[ 9] = new OffsetNode( new ContainerHeightNode( widget), quad.b + quad.d);
      }
      return nodes[ 9];
    }    
  }

  private IXidget xidget;
  private IComputeNode[] nodes;
}
