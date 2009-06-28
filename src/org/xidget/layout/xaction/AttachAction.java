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
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
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
    IModelObject element = context.getObject();
    if ( xidgetExpr != null) element = xidgetExpr.queryFirst( context);
    
    // get xidget for which attachments are being created
    IXidget xidget = (IXidget)element.getAttribute( "xidget");
    if ( xidget == null) return;
    
    // create nodes for attachments
    for( Attachment attachment: attachments)
      createNodes( attachment, context, xidget);
  }
      
  /**
   * Attach anchors of the specified xidget based on the specified attachment.
   * @param attachment The attachment.
   * @param context The context.
   * @param xidget1 The xidget.
   */
  private void createNodes( Attachment attachment, IContext context, IXidget xidget1)
  {
    StatefulContext context1 = new StatefulContext( context, xidget1.getConfig());
    
    IXidget xidget2 = xidget1.getParent();
    if ( attachment.xidgetExpr != null)
    {
      IModelObject xidgetNode = attachment.xidgetExpr.queryFirst( context);
      if ( xidgetNode == null) return;
    
      xidget2 = (IXidget)xidgetNode.getAttribute( "xidget");
    }
    
    IComputeNode anchor1 = getComputeNode( xidget1, attachment.anchor1);
    
    // override all other dependencies with the new ones created here
    anchor1.clearDependencies();
    
    IComputeNode anchor2 = getComputeNode( xidget2, attachment.anchor2);
    
    // must either have anchor or constant expression
    if ( anchor2 == null && attachment.constantExpr == null) return;
    
    // cases
    if ( attachment.constantExpr != null)
    {
      int constant = (int)attachment.constantExpr.evaluateNumber( context1);
      anchor1.addDependency( new ConstantNode( constant));
    }
    else if ( attachment.percentExpr != null)
    {
      float percent = (float)attachment.percentExpr.evaluateNumber( context1);
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( context1): 0;
      anchor1.addDependency( new ProportionalNode( anchor2, percent, offset));
    }
    else if ( attachment.offsetExpr != null)
    {
      int offset = (int)attachment.offsetExpr.evaluateNumber( context1);
      anchor1.addDependency( new OffsetNode( anchor2, offset));
    }
    else
    {
      anchor1.addDependency( anchor2);
    }

    // always use the xidget in the context since xidget1 may be the container, itself
    IXidget parent = (IXidget)context.getObject().getAttribute( "xidget");
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    layoutFeature.addNode( anchor1);
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
  }

  private final static IExpression thisExpr = XPath.createExpression( ".");
  
  private IExpression xidgetExpr;
  private List<Attachment> attachments;
}
