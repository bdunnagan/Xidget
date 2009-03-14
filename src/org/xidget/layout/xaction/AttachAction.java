/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import org.xidget.IXidget;
import org.xidget.config.XidgetMap;
import org.xidget.feature.IWidgetFeature;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.ProportionalNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An action which defines one to four attachments for a xidget. The <i>xidgets</i> variable
 * must be set to a Map<IModelObject, IXidget> which contains the mapping of configuration
 * elements to the xidget which are children of the container to which this layout is being
 * applied.
 */
public class AttachAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
   
    IModelObject root = document.getRoot();
    x0 = createAttachment( root.getFirstChild( "x0"));
    y0 = createAttachment( root.getFirstChild( "y0"));
    x1 = createAttachment( root.getFirstChild( "x1"));
    y1 = createAttachment( root.getFirstChild( "y1"));
  }
  
  /**
   * Create an attachment from the specified element.
   * @param element The element describing the attachment.
   * @return Returns the attachment.
   */
  private Attachment createAttachment( IModelObject element)
  {
    if ( element == null) return null;
    Attachment attachment = new Attachment();
    attachment.expr = Xlate.get( element, "expr", (IExpression)null);
    attachment.side = Xlate.get( element, "side", "");
    attachment.percent = Xlate.get( element, "percent", -1f);
    attachment.pad = Xlate.get( element, "pad", -1);
    return attachment;
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    map = getXidgets( context);
    
    // get xidget for which attachments are being created
    IXidget xidget = map.getXidget( context.getObject());
    IWidgetFeature widget = xidget.getFeature( IWidgetFeature.class);
    
    IXidget parent = xidget.getParent();
    IWidgetFeature container = parent.getFeature( IWidgetFeature.class);

    // create nodes
    if ( x0 != null) createNode( context, "x0", x0, container, xidget, widget);
    if ( y0 != null) createNode( context, "y0", y0, container, xidget, widget);
    if ( x1 != null) createNode( context, "x1", x1, container, xidget, widget);
    if ( y1 != null) createNode( context, "y1", y1, container, xidget, widget);
  }
  
  /**
   * Returns the enclosing xidget.
   * @param context The context.
   * @return Returns the enclosing xidget.
   */
  private static XidgetMap getXidgets( IContext context)
  {
    IModelObject holder = xidgetMapExpr.queryFirst( context);
    return (XidgetMap)holder.getValue();
  }
  
  /**
   * Get the peer of the attachment defined by the specified expression.
   * @param context The context.
   * @param peerExpr The peer expression.
   * @return Returns the peer of an attachment.
   */
  private IXidget getPeer( IContext context, IExpression peerExpr)
  {
    IModelObject element = peerExpr.queryFirst( context);
    return map.getXidget( element);
  }
  
  /**
   * Create an attachment for the specified widget.
   * @param context The context.
   * @param container The container.
   * @param widget The widget.
   * @param attachment The attachment.
   */
  private void createNode( IContext context, String side, Attachment attachment, IWidgetFeature container, IXidget xidget, IWidgetFeature widget)
  {
    IXidget peer = getPeer( context, attachment.expr);
    if ( peer == null) return;
    
    IWidgetFeature peerWidget = peer.getFeature( IWidgetFeature.class);
    if ( peerWidget != container)
    {
      if ( side.charAt( 1) == '1') attachment.pad = -attachment.pad;
      IComputeNode node1 = xidget.getAnchor( side);
      IComputeNode node2 = peer.getAnchor( attachment.side);
      if ( attachment.pad != 0)
      {
        node1.addDependency( new OffsetNode( node2, attachment.pad));
      }
      else
      {
        node1.addDependency( node2);
      }
    }
    else
    {
      if ( attachment.percent >= 0) side = (side.charAt( 0) == 'x')? "w": "h";
      IComputeNode node1 = xidget.getAnchor( side);
      IComputeNode node2 = peer.getAnchor( attachment.side);
      node1.addDependency( new ProportionalNode( node2, attachment.percent, attachment.pad));
    }
  }
  
  final class Attachment
  {
    IExpression expr;
    String side;
    int pad;
    float percent;
  }
  
  private final static IExpression xidgetMapExpr = XPath.createExpression( "$map");
  
  private XidgetMap map;
  private Attachment x0;
  private Attachment y0;
  private Attachment x1;
  private Attachment y1;
}
