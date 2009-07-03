/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.ProportionalNode;
import org.xidget.layout.XGrabNode;
import org.xidget.layout.YGrabNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xml.XmlIO;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

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
   
    xidgetExpr = document.getExpression( "xidget", true);
    IModelObject root = document.getRoot();
    
    attachments = new ArrayList<Attachment>();
    for( Type type: Type.values())
    {
      IModelObject element = root.getFirstChild( type.name());
      if ( element != null)
      {
        Attachment attachment = new Attachment();
        attachment.anchor1 = type;
        attachment.anchor2 = Type.valueOf( Xlate.get( element, "anchor", "none"));
        attachment.xidgetExpr = Xlate.get( element, "attach", thisExpr);
        attachment.constantExpr = Xlate.get( element, "constant", (IExpression)null);
        attachment.offsetExpr = Xlate.get( element, "offset", (IExpression)null);
        attachment.percentExpr = Xlate.get( element, "percent", (IExpression)null);
        attachment.handleExpr = Xlate.get( element, "handle", (IExpression)null);
        attachments.add( attachment);
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    // get container xidget
    IModelObject element = context.getObject();
    IXidget parent = (IXidget)element.getAttribute( "xidget");
    
    // evaluate child xidget
    if ( xidgetExpr != null) element = xidgetExpr.queryFirst( context);
    
    // no child xidget found
    if ( element == null) return;
    
    // get xidget for which attachments are being created
    IXidget xidget = (IXidget)element.getAttribute( "xidget");
    if ( xidget == null) return;
    
    // create nodes for attachments
    for( Attachment attachment: attachments)
      createNodes( attachment, context, parent, xidget);
  }
      
  /**
   * Attach anchors of the specified xidget based on the specified attachment.
   * @param attachment The attachment.
   * @param context The context.
   * @param xidget1 The xidget.
   */
  private void createNodes( Attachment attachment, IContext context, IXidget parent, IXidget xidget1)
  {
    StatefulContext context1 = new StatefulContext( context, xidget1.getConfig());
    
    IXidget xidget2 = parent;
    if ( attachment.xidgetExpr != null)
    {
      IModelObject xidgetNode = attachment.xidgetExpr.queryFirst( context);
      if ( xidgetNode == null) return;
    
      xidget2 = (IXidget)xidgetNode.getAttribute( "xidget");
    }
    
    // validation
    if ( xidget1 == xidget2)
      throw new XActionException( "Cannot make attachment to self: "+XmlIO.toString( getDocument().getRoot()));
    
    if ( xidget2 == parent && attachment.anchor2 != Type.none)
      throw new XActionException( "Cannot specify anchor when attaching to container: "+XmlIO.toString( getDocument().getRoot()));
    
    // select appropriate anchor for container attachment
    Type anchor2 = attachment.anchor2;
    if ( xidget2 == parent)
    {
      switch( attachment.anchor1)
      {
        case top:
        case bottom:
          anchor2 = Type.height;
          break;
          
        case left:
        case right:
          anchor2 = Type.width;
          break;
      }
    }
    
    // make attachments
    IComputeNode computeNode1 = getComputeNode( xidget1, attachment.anchor1);
    
    // override all other dependencies with the new ones created here
    computeNode1.clearDependencies();
    
    IComputeNode computeNode2 = getComputeNode( xidget2, anchor2);
    
    // must either have anchor or constant expression
    if ( computeNode2 == null && attachment.constantExpr == null) return;
    
    
    // cases
    if ( attachment.constantExpr != null)
    {
      if ( xidget1 == parent)
        throw new XActionException( "Containers cannot have constant attachments: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( xidget2 != parent)
        throw new XActionException( "Constant attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( anchor2 != Type.width && anchor2 != Type.height)
        throw new XActionException( "Constant attachment cannot be specified relative to an anchor: "+XmlIO.toString( getDocument().getRoot()));
      
      int constant = (int)attachment.constantExpr.evaluateNumber( context1, 0);
      computeNode1.addDependency( new ConstantNode( constant));
    }
    else if ( attachment.percentExpr != null)
    {
      if ( xidget1 == parent)
        throw new XActionException( "Containers cannot have proportional attachments: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( xidget2 != parent)
        throw new XActionException( "Proportional attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( (attachment.anchor1 == Type.left || attachment.anchor1 == Type.right) && anchor2 != Type.width)
        throw new XActionException( "Left or right attachment must be made proprotional to width of container: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( (attachment.anchor1 == Type.top || attachment.anchor1 == Type.bottom) && anchor2 != Type.height)
        throw new XActionException( "Top or bottom attachment must be made proprotional to height of container: "+XmlIO.toString( getDocument().getRoot()));
      
      float percent = (float)attachment.percentExpr.evaluateNumber( context1, 0);
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( context1, 0): 0;
      
      IComputeNode dependency = null;
      if ( attachment.handleExpr != null && attachment.handleExpr.evaluateBoolean( context, false))
      {
        if ( attachment.anchor1 == Type.left || attachment.anchor1 == Type.right)
          dependency = new XGrabNode( computeNode2, percent, offset);
        else
          dependency = new YGrabNode( computeNode2, percent, offset);
      }
      else
      {
        dependency = new ProportionalNode( computeNode2, percent, offset);
      }
      
      computeNode1.addDependency( dependency);
    }
    else if ( attachment.offsetExpr != null)
    {
      if ( xidget2 == parent && anchor2 != Type.width && anchor2 != Type.height)
        throw new XActionException( "Offset attachment to container must specify width or height: "+XmlIO.toString( getDocument().getRoot()));
      
      int offset = (int)attachment.offsetExpr.evaluateNumber( context1, 0);
      computeNode1.addDependency( new OffsetNode( computeNode2, offset));
    }
    else
    {
      computeNode1.addDependency( computeNode2);
    }

    // always use the xidget in the context since xidget1 may be the container, itself
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    layoutFeature.addNode( computeNode1);
  }
  
  /**
   * Returns the specified IComputeNode from the specified xidget.
   * @param xidget The xidget.
   * @param anchor The anchor type.
   * @return Returns the specified IComputeNode from the specified xidget.
   */
  private static IComputeNode getComputeNode( IXidget xidget, Type anchor)
  {
    if ( xidget == null || anchor == null) return null;
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    return computeNodeFeature.getComputeNode( anchor);
  }
  
  final class Attachment
  {
    Type anchor1;
    Type anchor2;
    IExpression xidgetExpr;
    IExpression offsetExpr;
    IExpression percentExpr;
    IExpression constantExpr;
    IExpression handleExpr;
  }

  private final static IExpression thisExpr = XPath.createExpression( ".");
  
  private IExpression xidgetExpr;
  private List<Attachment> attachments;
}
