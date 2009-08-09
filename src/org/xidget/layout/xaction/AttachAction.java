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
        attachment.anchor2 = Type.valueOf( Xlate.get( element, "anchor", element.getType()));
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
  protected Object[] doAction( IContext context)
  {
    // get container xidget
    IModelObject element = context.getObject();
    IXidget parent = (IXidget)element.getAttribute( "xidget");
    
    // evaluate child xidget
    if ( xidgetExpr != null) element = xidgetExpr.queryFirst( context);
    
    // no child xidget found
    if ( element == null) return null;
    
    // get xidget for which attachments are being created
    IXidget xidget = (IXidget)element.getAttribute( "xidget");
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
        
    // make attachments
    IComputeNode computeNode1 = getComputeNode( xidget1, attachment.anchor1, xidget1 == parent);
    IComputeNode computeNode2 = getComputeNode( xidget2, attachment.anchor2, xidget2 == parent);
    
    // must either have anchor or constant expression
    if ( computeNode2 == null && attachment.constantExpr == null) return;
    
    
    // cases
    if ( attachment.constantExpr != null)
    {
      if ( xidget1 == parent)
        throw new XActionException( "Containers cannot have constant attachments: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( xidget2 != parent)
        throw new XActionException( "Constant attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      float constant = (float)attachment.constantExpr.evaluateNumber( context1, 0);
      computeNode1.setDefaultValue( constant);
    }
    else if ( attachment.percentExpr != null)
    {
      if ( xidget1 == parent)
        throw new XActionException( "Containers cannot have proportional attachments: "+XmlIO.toString( getDocument().getRoot()));
      
      if ( xidget2 != parent)
        throw new XActionException( "Proportional attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      float percent = (float)attachment.percentExpr.evaluateNumber( context1, 0);
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( context1, 0): 0;
      
      AnchorNode anchor = new AnchorNode( xidget2, attachment.anchor1, percent, offset);
      anchor.setHandle( attachment.handleExpr != null && attachment.handleExpr.evaluateBoolean( context, false));
      computeNode1.addDependency( anchor);
    }
    else if ( attachment.offsetExpr != null)
    {
      int offset = (int)attachment.offsetExpr.evaluateNumber( context1, 0);
      computeNode1.addDependency( new OffsetNode( computeNode2, offset));
    }
    else
    {
      computeNode1.addDependency( computeNode2);
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
    return computeNodeFeature.getComputeNode( anchor, container);
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
