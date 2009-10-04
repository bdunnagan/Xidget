/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.OffsetNode;
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
public class LayoutAttachAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
   
    IModelObject root = document.getRoot();
    IModelObject layout = root.getParent().getFirstChild( "layout");
    if ( layout != null)
    {
      String marginsSpec = Xlate.get( layout, "margins", Xlate.childGet( layout, "margins", (String)null));
      marginsExpr = XPath.createExpression ( marginsSpec);
      
      String spacingSpec = Xlate.get( layout, "spacing", Xlate.childGet( layout, "spacing", (String)null));
      spacingExpr = XPath.createExpression ( spacingSpec);
    }
    else
    {
      marginsExpr = XPath.createExpression( "'5'");
      spacingExpr = XPath.createExpression( "'5'");
    }

    // configure should only specify one of the following
    xidgetID = Xlate.get( root, "xid", (String)null);
    xidgetExpr = document.getExpression( "xidget", true);
    
    attachments = new ArrayList<Attachment>();
    for( Type type: Type.values())
    {
      IModelObject element = root.getFirstChild( type.name());
      if ( element != null)
      {
        Attachment attachment = new Attachment();
        attachment.anchor1 = type;
        attachment.anchor2 = Type.valueOf( Xlate.get( element, "anchor", element.getType()));
        attachment.xidgetExpr = Xlate.get( element, "attach", thisExpr);
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
  protected Object[] doAction( IContext context)
  {
    // get container xidget
    IModelObject element = context.getObject();
    IXidget parent = (IXidget)element.getAttribute( "instance");
    
    // evaluate child xidget
    if ( xidgetID != null)
    {
      for( IModelObject child: element.getChildren())
        if ( child.getID().equals( xidgetID))
          element = child;
    }
    else if ( xidgetExpr != null) 
    {
      element = xidgetExpr.queryFirst( context);
    }
    
    // no child xidget found
    if ( element == null) return null;
    
    // get xidget for which attachments are being created
    IXidget xidget = (IXidget)element.getAttribute( "instance");
    if ( xidget == null) return null;
    
    // create nodes for attachments
    for( Attachment attachment: attachments)
      createNodes( attachment, context, parent, xidget);
    
    return null;
  }
      
  /**
   * Attach anchors of the specified xidget based on the specified attachment.
   * @param attachment The attachment.
   * @param context The context.
   * @param xidget1 The xidget.
   */
  private void createNodes( Attachment attachment, IContext context, IXidget parent, IXidget xidget1)
  {
    StatefulContext configContext = new StatefulContext( context, xidget1.getConfig());
    
    // get parameters
    marginsExpr.setVariable( "containerType", parent.getConfig().getType());
    Margins margins = new Margins( marginsExpr.evaluateString( configContext));
    
    spacingExpr.setVariable( "containerType", parent.getConfig().getType());
    int spacing = (int)spacingExpr.evaluateNumber( configContext);
    
    // find other xidget
    IXidget xidget2 = parent;
    if ( attachment.xidgetExpr != null)
    {
      List<IXidget> children = parent.getChildren();
      int index = children.indexOf( xidget1);
      IXidget prev = (index == 0)? parent: children.get( index-1);
      IXidget next = (index == children.size() - 1)? parent: children.get( index+1);
      
      attachment.xidgetExpr.setVariable( "previous", prev.getConfig());
      attachment.xidgetExpr.setVariable( "next", next.getConfig());
      
      IModelObject xidgetNode = attachment.xidgetExpr.queryFirst( context);
      if ( xidgetNode == null) return;
      xidget2 = (IXidget)xidgetNode.getAttribute( "instance");
    }
    
    // validation
    if ( xidget1 == xidget2)
      throw new XActionException( "Cannot make attachment to self: "+XmlIO.toString( getDocument().getRoot()));

    if ( attachment.anchor2 == Type.nearest)
    {
      if ( xidget2 == parent) 
      {
        attachment.anchor2 = attachment.anchor1;
      }
      else
      {
        switch( attachment.anchor1)
        {
          case top: attachment.anchor2 = Type.bottom; break;
          case left: attachment.anchor2 = Type.right; break;
          case right: attachment.anchor2 = Type.left; break;
          case bottom: attachment.anchor2 = Type.top; break;
        }
      }
    }
    
    // make attachments
    IComputeNode computeNode1 = getComputeNode( xidget1, attachment.anchor1, xidget1 == parent);
    IComputeNode computeNode2 = getComputeNode( xidget2, attachment.anchor2, xidget2 == parent);
    
    // must either have anchor or constant expression
    if ( computeNode2 == null) return;
    
    int padding = 0;
    if ( xidget2 == parent)
    {
      switch( attachment.anchor2)
      {
        case top:    padding = margins.y0; break;
        case left:   padding = margins.x0; break;
        case right:  padding = -margins.x1; break;
        case bottom: padding = -margins.y1; break; 
      }
    }
    else if ( xidget1 == parent)
    {
      switch( attachment.anchor2)
      {
        case top:    padding = -margins.y0; break;
        case left:   padding = -margins.x0; break;
        case right:  padding = margins.x1; break;
        case bottom: padding = margins.y1; break; 
      }
    }
    else
    {
      switch( attachment.anchor1)
      {
        case top:    if ( attachment.anchor2 == Type.bottom) padding = spacing; break;
        case left:   if ( attachment.anchor2 == Type.right) padding = spacing; break;
        case right:  if ( attachment.anchor2 == Type.left) padding = -spacing; break;
        case bottom: if ( attachment.anchor2 == Type.top) padding = -spacing; break;
      }
    }
    
    // cases
    if ( attachment.percentExpr != null)
    {
      if ( xidget1 == parent)
        throw new XActionException( "Containers cannot have proportional attachments: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( xidget2 != parent)
        throw new XActionException( "Proportional attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      float percent = (float)attachment.percentExpr.evaluateNumber( configContext, 0);
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( configContext, 0): 0;
      
      AnchorNode anchor = new AnchorNode( xidget2, attachment.anchor1, percent, offset + padding);
      anchor.setHandle( attachment.handleExpr != null && attachment.handleExpr.evaluateBoolean( context, false));
      computeNode1.addDependency( anchor);
    }
    else if ( attachment.offsetExpr != null)
    {
      int offset = (int)attachment.offsetExpr.evaluateNumber( configContext, 0);
      computeNode1.addDependency( new OffsetNode( computeNode2, offset + padding));
    }
    else
    {
      computeNode1.addDependency( new OffsetNode( computeNode2, padding));
    }
  }
  
  /**
   * Returns the specified IComputeNode from the specified xidget.
   * @param xidget The xidget.
   * @param anchor The anchor type.
   * @param container True if the node is for attaching to the inside of the container.
   * @return Returns the specified IComputeNode from the specified xidget.
   */
  private static IComputeNode getComputeNode( IXidget xidget, Type anchor, boolean container)
  {
    if ( xidget == null || anchor == null) return null;
    
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    return computeNodeFeature.getComputeNode( anchor, container, true);
  }
  
  final class Attachment
  {
    Type anchor1;
    Type anchor2;
    IExpression xidgetExpr;
    IExpression offsetExpr;
    IExpression percentExpr;
    IExpression handleExpr;
  }

  private final static IExpression thisExpr = XPath.createExpression( ".");
  
  private String xidgetID;
  private IExpression xidgetExpr;
  private List<Attachment> attachments;
  private IExpression marginsExpr;
  private IExpression spacingExpr;
}
