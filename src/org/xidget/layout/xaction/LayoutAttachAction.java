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
import java.util.Collections;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.ILayoutFeature.Side;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.layout.AverageNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.MaximumNode;
import org.xidget.layout.MinimumNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.log.Log;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
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
          log.warn( "Deprecated use of 'anchor' attribute - use 'side' attribute instead.");
          element.setAttribute( "side", sideName);
          element.removeAttribute( "anchor");
        }

        Attachment attachment = new Attachment();
        
        attachment.side1 = side;
        attachment.side2 = Side.valueOf( Xlate.get( element, "side", element.getType()));
        attachment.xidgetsExpr = Xlate.get( element, "attach", containerExpr);
        attachment.offsetExpr = Xlate.get( element, "offset", (IExpression)null);
        attachment.percentExpr = Xlate.get( element, "percent", (IExpression)null);
        attachment.handleExpr = Xlate.get( element, "handle", (IExpression)null);
        
        String compute = Xlate.get( element, "compute", "maximum");
        if ( compute.equals( "minimum")) attachment.minAvgMax = new MinimumNode();
        if ( compute.equals( "average")) attachment.minAvgMax = new AverageNode();
        if ( compute.equals( "maximum")) attachment.minAvgMax = new MaximumNode();
        
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
   * @param xidget The xidget.
   */
  private void createNodes( Creator creator, Attachment attachment, IContext context, IXidget parent, IXidget xidget)
  {
    StatefulContext configContext = new StatefulContext( context, xidget.getConfig());
    
    // find other xidget
    List<IXidget> sites = Collections.singletonList( parent); // TODO: indicate with null instead to reduce garbage
    if ( attachment.xidgetsExpr != null)
    {
      List<IXidget> children = parent.getChildren();
      int index = children.indexOf( xidget);
      if ( index >= 0)
      {
        IXidget prev = (index == 0)? parent: children.get( index-1);
        IXidget next = (index == children.size() - 1)? parent: children.get( index+1);
        attachment.xidgetsExpr.setVariable( "previous", prev.getConfig());
        attachment.xidgetsExpr.setVariable( "next", next.getConfig());
      }
      
      List<IModelObject> xidgetNodes = AbstractLayoutAction.getTargets( attachment.xidgetsExpr, context);
      if ( xidgetNodes.size() == 0) return;

      sites = new ArrayList<IXidget>();
      for( IModelObject xidgetNode: xidgetNodes)
        sites.add( creator.findXidget( xidgetNode));
    }
    
    // get features
    ILayoutFeature layoutFeature = parent.getFeature( ILayoutFeature.class);
    IWidgetContainerFeature containerFeature = parent.getFeature( IWidgetContainerFeature.class);
    
    // cases
    if ( attachment.percentExpr != null)
    {
      IXidget site = sites.get( 0);

      if ( sites.size() > 1) 
      {
        throw new XActionException( getDocument(), "Too many xidgets specified with proportional attachment");
      }
      
      if ( xidget == parent)
      {
        throw new XActionException( getDocument(), "Containers cannot have proportional attachments");
      }
      
      if ( site != parent) 
      {
        throw new XActionException( getDocument(), "Proportional attachments must be specified relative to the container");
      }
      
      float percent = (float)attachment.percentExpr.evaluateNumber( configContext, 0);
      IModelObject percentNode = 
        (attachment.percentExpr.getType( configContext) == ResultType.NODES)? 
        attachment.percentExpr.queryFirst( configContext): null;
      
      int offset = (attachment.offsetExpr != null)? (int)attachment.offsetExpr.evaluateNumber( configContext, 0): 0;
      boolean handle = (attachment.handleExpr != null)? attachment.handleExpr.evaluateBoolean( configContext): false;
      
      layoutFeature.attachContainer( xidget, attachment.side1, percent, percentNode, offset, handle);
    }
    else
    {
      IXidget site = sites.get( 0);
      if ( site == parent) 
      {
        int offset = 0;
        if ( attachment.offsetExpr != null) offset = (int)attachment.offsetExpr.evaluateNumber( configContext, 0);
        layoutFeature.attachContainer( xidget, attachment.side1, attachment.side2, offset);
      }
      else 
      {
        int offset = 0;
        if ( attachment.side1 == Side.left && attachment.side2 == Side.right) offset = containerFeature.getSpacing();
        if ( attachment.side1 == Side.right && attachment.side2 == Side.left) offset = -containerFeature.getSpacing();
        if ( attachment.side1 == Side.top && attachment.side2 == Side.bottom) offset = containerFeature.getSpacing();
        if ( attachment.side1 == Side.bottom && attachment.side2 == Side.top) offset = -containerFeature.getSpacing();
        if ( attachment.offsetExpr != null) offset = (int)attachment.offsetExpr.evaluateNumber( configContext, 0);
        if ( sites.size() == 1)
        {
          layoutFeature.attachPeer( xidget, attachment.side1, site, attachment.side2, offset);
        }
        else
        {
          if ( attachment.minAvgMax == null) attachment.minAvgMax = new MaximumNode();
          layoutFeature.attachPeers( xidget, attachment.side1, sites, attachment.side2, offset, attachment.minAvgMax);
        }
      }
    }
  }
  
  final class Attachment
  {
    Side side1;
    Side side2;
    IExpression xidgetsExpr;
    IExpression offsetExpr;
    IExpression percentExpr;
    IExpression handleExpr;
    IComputeNode minAvgMax;
  }

  private final static Log log = Log.getLog( LayoutAttachAction.class);
  private final static IExpression containerExpr = XPath.createExpression( ".");
  
  private IExpression xidgetExpr;
  private List<Attachment> attachments;
}
