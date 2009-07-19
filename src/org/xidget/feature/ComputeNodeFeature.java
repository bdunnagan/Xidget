/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.SubtractNode;
import org.xidget.layout.SumNode;
import org.xidget.layout.WidgetHandle;

/**
 * An implementation of IComputeNodeFeature.
 */
public class ComputeNodeFeature implements IComputeNodeFeature
{
  public ComputeNodeFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /**
   * Returns a name for a constant node.
   * @param xidget The xidget.
   * @param type The type.
   * @param inside True if inside node.
   * @return Returns a name for a constant node.
   */
  private static String getName( IXidget xidget, Type type, boolean inside)
  {
    return String.format( "%s:%s:%s", xidget, type, inside? "inside": "outside");
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getComputeNode(org.xidget.ifeature.IComputeNodeFeature.Type, boolean)
   */
  public IComputeNode getComputeNode( Type type, boolean container)
  {
    IWidgetContainerFeature containerFeature = xidget.getFeature( IWidgetContainerFeature.class);
    Margins margins = (containerFeature != null)? containerFeature.getInsideMargins(): new Margins();
    
    if ( container)
    {
      switch( type)
      {
        case top:
          if ( insideY0 == null)
          {
            insideY0 = new WidgetHandle( getName( xidget, type, container), margins.y0);
            insideY0.setDefaultValue( 0f);
          }
          return insideY0;
          
        case left:   
          if ( insideX0 == null) 
          {
            insideX0 = new WidgetHandle( getName( xidget, type, container), margins.x0);
            insideX0.setDefaultValue( 0f);
          }
          return insideX0;
          
        case right:
          if ( insideX1 == null) 
          {
            insideX1 = new WidgetHandle( getName( xidget, type, container), 0);
            insideX1.addDependency( new OffsetNode( getComputeNode( Type.width, true), -margins.x1));
          }
          return insideX1;

        case bottom:
          if ( insideY1 == null) 
          {
            insideY1 = new WidgetHandle( getName( xidget, type, container), 0);
            insideY1.addDependency( new OffsetNode( getComputeNode( Type.height, true), -margins.y1));
          }
          return insideY1;
          
        case width:
          if ( insideWidth == null) insideWidth = new WidgetHandle( getName( xidget, type, container), 0);
          return insideWidth;
          
        case height:
          if ( insideHeight == null) insideHeight = new WidgetHandle( getName( xidget, type, container), 0);
          return insideHeight;
          
        default:     return null;
      }
    }
    else
    {
      switch( type)
      {
        case top:
          if ( outsideY0 == null) 
          {
            outsideY0 = new WidgetHandle( getName( xidget, type, container), 0);
            outsideY0.addDependency( new SubtractNode( getComputeNode( Type.bottom, false), getComputeNode( Type.height, false)));
          }
          return outsideY0;
          
        case left:
          if ( outsideX0 == null) 
          {
            outsideX0 = new WidgetHandle( getName( xidget, type, container), 0);
            outsideX0.addDependency( new SubtractNode( getComputeNode( Type.right, false), getComputeNode( Type.width, false)));
          }
          return outsideX0;
          
        case right:
          if ( outsideX1 == null) 
          {
            outsideX1 = new WidgetHandle( getName( xidget, type, container), 0);
            outsideX1.addDependency( new SumNode( getComputeNode( Type.left, false), getComputeNode( Type.width, false)));
          }
          return outsideX1;
          
        case bottom:
          if ( outsideY1 == null) 
          {
            outsideY1 = new WidgetHandle( getName( xidget, type, container), 0);
            outsideY1.addDependency( new SumNode( getComputeNode( Type.top, false), getComputeNode( Type.height, false)));
          }
          return outsideY1;
                    
        case width:
          if ( outsideWidth == null)
          {
            outsideWidth = new WidgetHandle( getName( xidget, type, container), 0);
            
            IComputeNode width = new SubtractNode( getComputeNode( Type.right, false), getComputeNode( Type.left, false));
            outsideWidth.addDependency( width);
          }
          return outsideWidth;
          
        case height:
          if ( outsideHeight == null)
          {
            outsideHeight = new WidgetHandle( getName( xidget, type, container), 0);
            
            IComputeNode height = new SubtractNode( getComputeNode( Type.bottom, false), getComputeNode( Type.top, false));
            outsideHeight.addDependency( height);
          }
          return outsideHeight;
          
        default:     return null;
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getAccessedList()
   */
  public List<IComputeNode> getAccessedList()
  {
    List<IComputeNode> nodes = new ArrayList<IComputeNode>( 12);
    
    if ( insideX0 != null) nodes.add( insideX0);
    if ( insideY0 != null) nodes.add( insideY0);
    if ( insideX1 != null) nodes.add( insideX1);
    if ( insideY1 != null) nodes.add( insideY1);
    if ( insideWidth != null) nodes.add( insideWidth);
    if ( insideHeight != null) nodes.add( insideHeight);
    
    if ( outsideX0 != null) nodes.add( outsideX0);
    if ( outsideY0 != null) nodes.add( outsideY0);
    if ( outsideX1 != null) nodes.add( outsideX1);
    if ( outsideY1 != null) nodes.add( outsideY1);
    if ( outsideWidth != null) nodes.add( outsideWidth);
    if ( outsideHeight != null) nodes.add( outsideHeight);
    
    return nodes;
  }

  protected IXidget xidget;
  protected IComputeNode insideX0;
  protected IComputeNode insideX1;
  protected IComputeNode insideY0;
  protected IComputeNode insideY1;
  protected IComputeNode insideWidth;
  protected IComputeNode insideHeight;
  protected IComputeNode outsideX0;
  protected IComputeNode outsideX1;
  protected IComputeNode outsideY0;
  protected IComputeNode outsideY1;
  protected IComputeNode outsideWidth;
  protected IComputeNode outsideHeight;
}
