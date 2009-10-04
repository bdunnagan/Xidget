/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.Size;
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
    String name = String.format( "%s:%s:%s", xidget, type, inside? "inside": "outside");
    if ( name.length() > 60) name = name.substring( name.length() - 60);
    return name;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IComputeNodeFeature#getComputeNode(org.xidget.ifeature.IComputeNodeFeature.Type, boolean)
   */
  public IComputeNode getComputeNode( Type type, boolean container, boolean create)
  {
    if ( container)
    {
      switch( type)
      {
        case top:
          if ( insideY0 == null && create)
          {
            insideY0 = new WidgetHandle( getName( xidget, type, container), 0);
            insideY0.setDefaultValue( 0f);
          }
          return insideY0;
          
        case left:   
          if ( insideX0 == null && create) 
          {
            insideX0 = new WidgetHandle( getName( xidget, type, container), 0);
            insideX0.setDefaultValue( 0f);
          }
          return insideX0;
          
        case right:
          if ( insideX1 == null && create) insideX1 = new WidgetHandle( getName( xidget, type, container), 0);
          return insideX1;

        case bottom:
          if ( insideY1 == null && create) insideY1 = new WidgetHandle( getName( xidget, type, container), 0);
          return insideY1;
          
        default:     return null;
      }
    }
    else
    {
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      Size size = new Size(); widgetFeature.getPreferredSize( size);
      
      switch( type)
      {
        case top:
          if ( outsideY0 == null && create)
          {
            outsideY0 = new WidgetHandle( getName( xidget, type, container), 0);
            if ( size.height > 0) outsideY0.addDependency( new OffsetNode( getComputeNode( Type.bottom, false, create), -size.height));
          }
          return outsideY0;
          
        case left:
          if ( outsideX0 == null && create)
          {
            outsideX0 = new WidgetHandle( getName( xidget, type, container), 0);
            if ( size.width > 0) outsideX0.addDependency( new OffsetNode( getComputeNode( Type.right, false, create), -size.width));
          }
          return outsideX0;
          
        case right:
          if ( outsideX1 == null && create) 
          {
            outsideX1 = new WidgetHandle( getName( xidget, type, container), 0);
            if ( size.width > 0) outsideX1.addDependency( new OffsetNode( getComputeNode( Type.left, false, create), size.width));
          }
          return outsideX1;
          
        case bottom:
          if ( outsideY1 == null && create) 
          {
            outsideY1 = new WidgetHandle( getName( xidget, type, container), 0);
            if ( size.height > 0) outsideY1.addDependency( new OffsetNode( getComputeNode( Type.top, false, create), size.height));
          }
          return outsideY1;
          
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
    
    if ( outsideX0 != null) nodes.add( outsideX0);
    if ( outsideY0 != null) nodes.add( outsideY0);
    if ( outsideX1 != null) nodes.add( outsideX1);
    if ( outsideY1 != null) nodes.add( outsideY1);
    
    return nodes;
  }

  protected IXidget xidget;
  protected IComputeNode insideX0;
  protected IComputeNode insideX1;
  protected IComputeNode insideY0;
  protected IComputeNode insideY1;
  protected IComputeNode outsideX0;
  protected IComputeNode outsideX1;
  protected IComputeNode outsideY0;
  protected IComputeNode outsideY1;
}
