/*
 * Xidget - XML Widgets based on JAHM
 * 
 * LayoutAttachAction.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xidget.layout.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xml.XmlIO;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpression.ResultType;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction that creates one or more attachments of any type for one xidget. The target xidget
 * can be identified by either its <i>id</i> or its configuration element. In the latter case
 * the affected xidget will be the xidget that was last created from the configuration element.
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

    // configure should only specify one of the following
    xidgetExpr = document.getExpression( "xidget", true);
    if ( xidgetExpr == null) xidgetExpr = document.getExpression( "xidgets", true);
    
    attachments = new ArrayList<Attachment>();
    for( Side side: Side.values())
    {
      IModelObject element = root.getFirstChild( side.name());
      if ( element != null)
      {
        String sideName = Xlate.get( element, "anchor", (String)null);
        if ( sideName != null)
        {
          System.err.println( "Warning: deprecated use of 'anchor' attribute. Use 'side' instead.");
          element.setAttribute( "side", sideName);
          element.removeAttribute( "anchor");
        }

        Attachment attachment = new Attachment();
        attachment.side1 = side;
        attachment.side2 = Side.valueOf( Xlate.get( element, "side", element.getType()));
        attachment.xidgetExpr = Xlate.get( element, "attach", containerExpr);
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
    Creator creator = Creator.getInstance();
    
    // get container xidget
    IModelObject parentElement = context.getObject();
    IXidget parent = creator.findXidget( parentElement);
    
    // evaluate child xidgets
    if ( xidgetExpr == null) return null;
    for( IModelObject element: AbstractLayoutAction.getTargets( xidgetExpr, context))
    {
      // get xidget for which attachments are being created
      IXidget xidget = creator.findXidget( element);
      if ( xidget == null) return null;
      
      // create nodes for attachments
      for( Attachment attachment: attachments)
        createNodes( creator, attachment, context, parent, xidget);
    }
    
    return null;
  }
      
  /**
   * Attach anchors of the specified xidget based on the specified attachment.
   * @parma creator The Creator instance.
   * @param attachment The attachment.
   * @param context The context.
   * @param xidget1 The xidget.
   */
  private void createNodes( Creator creator, Attachment attachment, IContext context, IXidget parent, IXidget xidget1)
  {
    StatefulContext configContext = new StatefulContext( context, xidget1.getConfig());
    
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
      
      List<IModelObject> xidgetNodes = AbstractLayoutAction.getTargets( attachment.xidgetExpr, context);
      if ( xidgetNodes.size() == 0) return;
      xidget2 = creator.findXidget( xidgetNodes.get( 0));
    }
    
    // validation
    if ( xidget1 == xidget2) throw new XActionException( "Cannot make attachment to self: "+XmlIO.toString( getDocument().getRoot()));

    // get features
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    IWidgetContainerFeature containerFeature = parent.getFeature( IWidgetContainerFeature.class);
    
    // cases
    if ( attachment.percentExpr != null)
    {
      if ( xidget1 == parent) throw new XActionException( "Containers cannot have proportional attachments: "+XmlIO.toString( getDocument().getRoot()));
      if ( xidget2 != parent) throw new XActionException( "Proportional attachments must be specified relative to the container: "+XmlIO.toString( getDocument().getRoot()));
      
      float percent = (float)attachment.percentExpr.evaluateNumber( configContext, 0);
      IModelObject percentNode = (attachment.percentExpr.getType( configContext) == ResultType.NODES)? 
        attachment.percentExpr.queryFirst( configContext): null;
      
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( configContext, 0): 0;
      boolean handle = (attachment.handleExpr != null)? attachment.handleExpr.evaluateBoolean( configContext): false;
      
      layoutFeature.attachContainer( xidget1, attachment.side1, percent, percentNode, offset, handle);
    }
    else
    {
      if ( xidget2 == parent) 
      {
        int offset = 0;
        if ( attachment.offsetExpr != null) offset = (int)attachment.offsetExpr.evaluateNumber( configContext, 0);
        layoutFeature.attachContainer( xidget1, attachment.side1, offset);
      }
      else 
      {
        int offset = containerFeature.getSpacing();
        if ( attachment.side1 == Side.right && attachment.side2 == Side.left) offset = -offset;
        if ( attachment.side1 == Side.bottom && attachment.side2 == Side.top) offset = -offset;
        if ( attachment.offsetExpr != null) offset = (int)attachment.offsetExpr.evaluateNumber( configContext, 0);
        layoutFeature.attachPeer( xidget1, attachment.side1, xidget2, attachment.side2, offset);
      }
    }
  }
  
  final class Attachment
  {
    Side side1;
    Side side2;
    IExpression xidgetExpr;
    IExpression offsetExpr;
    IExpression percentExpr;
    IExpression handleExpr;
  }

  private final static IExpression containerExpr = XPath.createExpression( ".");
  
  private IExpression xidgetExpr;
  private List<Attachment> attachments;
}
