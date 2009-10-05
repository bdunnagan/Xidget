/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xpath.expression.IContext;

/**
 * An XAction that creates attachments for all xidgets in a container based on their attachment declarations.
 */
public class LayoutDeclarationAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IXidget parent = (IXidget)context.getObject().getAttribute( "instance");
    if ( parent == null) return null;

    List<IXidget> children = parent.getChildren();
    IXidget previous = parent;
    for( int i=0, j=1; i<children.size(); i++, j++)
    {
      IXidget next = (j < children.size())? children.get( j): parent;
      IXidget xidget = children.get( i);
      
      IModelObject attach = xidget.getConfig().getFirstChild( "attach");
      Type type = Type.valueOf( Xlate.get( attach, "type", (String)null));
      switch( type)
      {
        case containerSide:
          attachContainerSide( context, parent, xidget, attach);
          break;
          
        case containerFloat:
          attachContainerFloat( context, parent, xidget, attach);
          break;
          
        case previousPeer:
          attachPeer( context, parent, xidget, previous, attach);
          break;
          
        case nextPeer:
          attachPeer( context, parent, xidget, next, attach);
          break;
          
        case specificPeer:
          attachSpecificPeer( context, parent, xidget, children, attach);
          break;
          
        case none: break;
      }
    }
    
    return null;
  }
  
  /**
   * Create a containerSide attachment.
   * @param context The context.
   * @param parent The parent xidget.
   * @param xidget The xidget.
   * @param attach The attachment declaration.
   */
  private void attachContainerSide( IContext context, IXidget parent, IXidget xidget, IModelObject attach)
  {
    int offset = Xlate.get( attach, "offset", 0);
    IComputeNode parentNode = getComputeNode( parent, attach.getType(), true, false);
    IComputeNode xidgetNode = getComputeNode( xidget, attach.getType(), false, false);
    if ( offset > 0)
    {
      xidgetNode.addDependency( new OffsetNode( parentNode, offset));
    }
    else
    {
      xidgetNode.addDependency( parentNode);
    }
  }
  
  /**
   * Create a containerFloat attachment.
   * @param context The context.
   * @param parent The parent xidget.
   * @param xidget The xidget.
   * @param attach The attachment declaration.
   */
  private void attachContainerFloat( IContext context, IXidget parent, IXidget xidget, IModelObject attach)
  {
    int offset = Xlate.get( attach, "offset", 0);
    float percent = Xlate.get( attach, "percent", 0f);
    IComputeNodeFeature.Type side = IComputeNodeFeature.Type.valueOf( attach.getType());
    IComputeNode xidgetNode = getComputeNode( xidget, attach.getType(), false, false);
    xidgetNode.addDependency( new AnchorNode( parent, side, percent, offset));
  }
  
  /**
   * Create a containerFloat attachment.
   * @param context The context.
   * @param parent The parent xidget.
   * @param xidget The xidget.
   * @param peer The peer xidget.
   * @param attach The attachment declaration.
   */
  private void attachPeer( IContext context, IXidget parent, IXidget xidget, IXidget peer, IModelObject attach)
  {
    int offset = Xlate.get( attach, "offset", 0);
    String peerSide = Xlate.get( attach, "anchor", attach.getType());
    
    IComputeNode xidgetNode = getComputeNode( xidget, attach.getType(), false, false);
    IComputeNode peerNode = getComputeNode( peer, peerSide, false, attach.getAttribute( "anchor") == null);
    if ( offset > 0)
    {
      xidgetNode.addDependency( new OffsetNode( peerNode, offset));
    }
    else
    {
      xidgetNode.addDependency( peerNode);
    }
  }
  
  /**
   * Create a containerFloat attachment.
   * @param context The context.
   * @param parent The parent xidget.
   * @param xidget The xidget.
   * @param peers All of the possible peer xidgets.
   * @param attach The attachment declaration.
   */
  private void attachSpecificPeer( IContext context, IXidget parent, IXidget xidget, List<IXidget> peers, IModelObject attach)
  {
    String id = Xlate.get( attach, "peer", (String)null);
    for( IXidget peer: peers)
    {
      if ( peer.getConfig().getID().equals( id))
      {
        attachPeer( context, parent, xidget, peer, attach);
        break;
      }
    }
  }
  
  /**
   * Returns the IComputeNode for the specified xidget.
   * @param xidget The xidget.
   * @param side The side.
   * @param container True if the xidget is the container.
   * @param opposite True if the opposite of the specified side should be returned.
   * @return Returns the IComputeNode for the specified xidget.
   */
  private IComputeNode getComputeNode( IXidget xidget, String side, boolean container, boolean opposite)
  {
    IComputeNodeFeature.Type sideEnum = IComputeNodeFeature.Type.valueOf( side);
    if ( opposite) sideEnum = getOpposite( sideEnum);
    IComputeNodeFeature feature = xidget.getFeature( IComputeNodeFeature.class);
    return feature.getComputeNode( sideEnum, container, true);
  }
  
  /**
   * Returns the opposite side.
   * @param side The side.
   * @return Returns the opposite side.
   */
  private IComputeNodeFeature.Type getOpposite( IComputeNodeFeature.Type side)
  {
    switch( side)
    {
      case top: return IComputeNodeFeature.Type.bottom;
      case left: return IComputeNodeFeature.Type.right;
      case right: return IComputeNodeFeature.Type.left;
      case bottom: return IComputeNodeFeature.Type.top;
    }
    return IComputeNodeFeature.Type.none;
  }
  
  private enum Type { none, containerSide, containerFloat, previousPeer, nextPeer, specificPeer};
}
