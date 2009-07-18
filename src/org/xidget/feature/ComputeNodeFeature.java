/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

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
            insideX1 = new WidgetHandle( getName( xidget, type, container), -margins.x1);
            insideX1.addDependency( getComputeNode( Type.width, true));
          }
          return insideX1;

        case bottom:
          if ( insideY1 == null) 
          {
            insideY1 = new WidgetHandle( getName( xidget, type, container), -margins.y1);
            insideY1.addDependency( getComputeNode( Type.height, true));
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
          if ( outsideY0 == null) outsideY0 = new WidgetHandle( getName( xidget, type, container), 0);
          return outsideY0;
          
        case left:
          if ( outsideX0 == null) outsideX0 = new WidgetHandle( getName( xidget, type, container), 0);
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
            
            IComputeNode insideWidth = getComputeNode( Type.width, true);
            outsideWidth.addDependency( new OffsetNode( insideWidth, margins.x0 + margins.x1));
          }
          return outsideWidth;
          
        case height:
          if ( outsideHeight == null)
          {
            outsideHeight = new WidgetHandle( getName( xidget, type, container), 0);
            
            IComputeNode height = new SubtractNode( getComputeNode( Type.bottom, false), getComputeNode( Type.top, false));
            outsideHeight.addDependency( height);
            
            IComputeNode insideHeight = getComputeNode( Type.height, true);
            outsideHeight.addDependency( new OffsetNode( insideHeight, margins.y0 + margins.y1));
          }
          return outsideHeight;
          
        default:     return null;
      }
    }
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
