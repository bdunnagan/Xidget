/*
 * Xidget - XML Widgets based on JAHM
 * 
 * AnchorLayoutFeature.java
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
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ILabelFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.Bounds;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.InternalCenterBrace;
import org.xidget.layout.InternalDimensionBrace;
import org.xidget.layout.Margins;
import org.xidget.layout.MaximumNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.WidgetHandle;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.log.Log;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A layout algorithm where the layout contraints are specified as attachments (joints) between
 * xidgets. This layout algorithm is similar to the SWT FormLayout or Swing SpringLayout, but
 * provides consistent behavior across platforms.
 */
public class AnchorLayoutFeature implements ILayoutFeature
{
  public AnchorLayoutFeature( IXidget container)
  {
    this.xidget = container;
    this.groups = new HashMap<IXidget, NodeGroup>();
    this.sorted = null;
    this.groups.clear();
    this.width = 0;
    this.height = 0;
    
    //log.setLevel( Log.all);
  }
  
  public AnchorLayoutFeature( IXidget container, ScriptAction script)
  {
    this( container);
    this.script = script;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#layout()
   */
  public void layout()
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();
    
    if ( width == bounds.width && height == bounds.height) return;
    width = bounds.width; height = bounds.height;

    // initialize the container's inside nodes
    initContainerNodes();
    
    // calculate the preferred size of labeled xidgets
    initLabels();
    
    // compile layout
    if ( sorted == null) compile();
    
    // calculate layout
    if ( sorted != null)
    {
      for( IComputeNode node: sorted) node.reset();
      for( IComputeNode node: sorted) node.update();
      //for( IComputeNode node: sorted) node.update();
    }

    if ( log.isLevelEnabled( Log.debug)) logLayout();
    
    // update bounds of children
    updateChildrenBounds();
    
    // update container bounds
    updateContainerSize();
    
    // layout remaining children
    for( IXidget child: xidget.getChildren())
    {
      ILayoutFeature layoutFeature = child.getFeature( ILayoutFeature.class);
      if ( layoutFeature != null) layoutFeature.layout();
    }
  }
  
  /**
   * Initialize the container's inside nodes according to the margins.
   */
  private void initContainerNodes()
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Margins margins = widgetFeature.getInsideMargins();
   
    // TODO: eliminate garbage
    
    IComputeNode top = getCreateNode( xidget, Side.top);
    top.clearDependencies();
    top.addDependency( new ConstantNode( margins.y0));
    
    IComputeNode left = getCreateNode( xidget, Side.left);
    left.clearDependencies();
    left.addDependency( new ConstantNode( margins.x0));
    
    Bounds bounds = widgetFeature.getComputedBounds();
    
    if ( bounds.isWidthDefined())
    {
      IComputeNode right = getCreateNode( xidget, Side.right);
      right.clearDependencies();
      right.addDependency( new ConstantNode( bounds.width - margins.x1));
      
      IComputeNode hcenter = getCreateNode( xidget, Side.hcenter);
      hcenter.clearDependencies();
      hcenter.addDependency( new ConstantNode( (bounds.width / 2) - margins.x1));
    }
    
    if ( bounds.isHeightDefined())
    {
      IComputeNode bottom = getCreateNode( xidget, Side.bottom);
      bottom.clearDependencies();
      bottom.addDependency( new ConstantNode( bounds.height - margins.y1));
      
      IComputeNode vcenter = getCreateNode( xidget, Side.vcenter);
      vcenter.clearDependencies();
      vcenter.addDependency( new ConstantNode( (bounds.height / 2) - margins.y1));
    }
  }
  
  /**
   * Find the longest label among the labeled components and initialize
   * the size of all peer labels to the same size to provide a nice 
   * alignment. This algorithm is only applied if every peer has a label.
   */
  private void initLabels()
  {
    List<ILabelFeature> labelFeatures = new ArrayList<ILabelFeature>();
    
    int maxWidth = 0;
    for( IXidget child: xidget.getChildren())
    {
      ILabelFeature labelFeature = child.getFeature( ILabelFeature.class);
      
      int labelWidth = 0;
      if ( labelFeature != null) 
      {
        labelWidth = labelFeature.getLabelWidth();
      }
      
      if ( labelWidth == 0)
      {
        // flow may begin or end with unlabeled widgets
        if ( maxWidth > 0) break;
      }
      else
      {
        if ( labelWidth > maxWidth) maxWidth = labelWidth;
        labelFeatures.add( labelFeature);
      }
    }
    
    for( ILabelFeature labelFeature: labelFeatures)
      labelFeature.setLabelWidth( maxWidth);
  }
  
  /**
   * Update the bounds of each child based on the value of its nodes.
   */
  private void updateChildrenBounds()
  {
    for( Map.Entry<IXidget, NodeGroup> entry: groups.entrySet())
    {
      IXidget xidget = entry.getKey();
      NodeGroup group = entry.getValue();
      
      if ( xidget == this.xidget) continue;
      
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      Bounds bounds = widgetFeature.getComputedBounds();

      if ( !group.top.hasValue()) log.warnf( "%s: top is not defined.", xidget);
      if ( !group.bottom.hasValue()) log.warnf( "%s: bottom is not defined.", xidget);
      if ( !group.left.hasValue()) log.warnf( "%s: left is not defined.", xidget);
      if ( !group.right.hasValue()) log.warnf( "%s: right is not defined.", xidget);
      
      if ( group.top.hasValue() && group.bottom.hasValue())
      {
        bounds.y = group.top.getValue();
        bounds.height = group.bottom.getValue() - bounds.y;
      }
      
      if ( group.left.hasValue() && group.right.hasValue())
      {
        bounds.x = group.left.getValue();
        bounds.width = group.right.getValue() - bounds.x;
      }
      
      widgetFeature.setComputedBounds( bounds.x, bounds.y, bounds.width, bounds.height);
    }
  }
  
  /**
   * Update the size of the container based on its right and bottom nodes.
   */
  private void updateContainerSize()
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();
    
    IComputeNode right = getCreateNode( xidget, Side.right);
    IComputeNode bottom = getCreateNode( xidget, Side.bottom);

    Margins margins = widgetFeature.getInsideMargins();
    if ( right.hasValue()) bounds.width = right.getValue() + margins.x1;
    if ( bottom.hasValue()) bounds.height = bottom.getValue() + margins.y1;
    
    // set widget size
    widgetFeature.setComputedBounds( bounds.x, bounds.y, bounds.width, bounds.height);
  }
  
  /**
   * Returns all nodes including dependencies.
   * @return Returns all nodes including dependencies.
   */
  public List<IComputeNode> getAllNodes()
  {
    return sorted;
  }

  /**
   * Execute the layout script and sort the computation nodes.
   */
  private void compile()
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = bindFeature.getBoundContext();
    if ( context == null)
    {
      log.errorf( "%s: unable to compile layout for unbound xidget.", xidget);
      return;
    }
    
    if ( script == null) loadScript( context);
      
    // execute script
    if ( script != null)
    {
      StatefulContext scriptContext = new StatefulContext( context, xidget.getConfig());
      script.run( scriptContext);
    }
    else
    {
      // create default script
      createDefaultLayout();
    }
    
    // add internal bracing
    for( IXidget child: xidget.getChildren())
      addInternalBraces( child);
        
    // get list of defined nodes
    List<IComputeNode> nodes = new ArrayList<IComputeNode>();
    for( NodeGroup group: groups.values())
    {
      if ( group.top != null) nodes.add( group.top);
      if ( group.left != null) nodes.add( group.left);
      if ( group.right != null) nodes.add( group.right);
      if ( group.bottom != null) nodes.add( group.bottom);
      if ( group.hcenter != null) nodes.add( group.hcenter);
      if ( group.vcenter != null) nodes.add( group.vcenter);
    }
    
    // sort nodes
    sorted = sort( nodes);
  }
  
  /**
   * Load the layout script.
   * @param context The context.
   */
  private void loadScript( StatefulContext context)
  {
    // load script pointed to by layout attribute
    IModelObject config = xidget.getConfig();
    if ( config.getAttribute( "layout") != null)
    {
      IExpression layoutExpr = Xlate.get( config, "layout", (IExpression)null);
      IModelObject element = layoutExpr.queryFirst( context);
      if ( element != null)
      {
        XActionDocument doc = new XActionDocument( element);
        doc.addPackage( "org.xidget.layout.xaction");
        ClassLoader loader = getClass().getClassLoader();
        doc.setClassLoader( loader);
        script = doc.createScript();
      }
    }
    
    // load script in layout child
    else
    {
      IModelObject layout = config.getFirstChild( "layout");
      if ( layout != null)
      {
        XActionDocument doc = new XActionDocument( layout);
        doc.addPackage( "org.xidget.layout.xaction");
        ClassLoader loader = getClass().getClassLoader();
        doc.setClassLoader( loader);
        script = doc.createScript();
      }
    }
  }
  
  /**
   * Add internal braces to the specified node if one of its sides is not constrained.
   * @param xidget The xidget.
   */
  private void addInternalBraces( IXidget child)
  {
    NodeGroup group = getNodeGroup( child);
    boolean isTopFree = isNodeFree( group.top);
    boolean isBottomFree = isNodeFree( group.bottom);
    boolean isLeftFree = isNodeFree( group.left);
    boolean isRightFree = isNodeFree( group.right);
    boolean isHCenterFree = isNodeFree( group.hcenter);
    boolean isVCenterFree = isNodeFree( group.vcenter);

    //
    // Case 1: One edge and center are free
    //
    if ( isTopFree && isVCenterFree && !isBottomFree)
    {
      group.top.addDependency( new InternalDimensionBrace( "BTBrace", child, group.bottom, Side.bottom, Side.top));
      group.vcenter.addDependency( new InternalDimensionBrace( "BCBrace", child, group.bottom, Side.bottom, Side.vcenter));
      isTopFree = false;
      isVCenterFree = false;
    }
    else if ( isBottomFree && isVCenterFree && !isTopFree)
    {
      group.bottom.addDependency( new InternalDimensionBrace( "TBBrace", child, group.top, Side.top, Side.bottom));
      group.vcenter.addDependency( new InternalDimensionBrace( "TCBrace", child, group.top, Side.top, Side.vcenter));
      isBottomFree = false;
      isVCenterFree = false;
    }
    
    if ( isLeftFree && isHCenterFree && !isRightFree)
    {
      group.left.addDependency( new InternalDimensionBrace( "RLBrace", child, group.right, Side.right, Side.left));
      group.hcenter.addDependency( new InternalDimensionBrace( "RCBrace", child, group.right, Side.right, Side.hcenter));
      isLeftFree = false;
      isHCenterFree = false;
    }
    else if ( isRightFree && isHCenterFree && !isLeftFree)
    {
      group.right.addDependency( new InternalDimensionBrace( "LRBrace", child, group.left, Side.left, Side.right));
      group.hcenter.addDependency( new InternalDimensionBrace( "LCBrace", child, group.left, Side.left, Side.hcenter));
      isRightFree = false;
      isHCenterFree = false;
    }
    
    //
    // Case 2: Both edges are free, center is constrained
    //
    if ( isTopFree && isBottomFree && !isVCenterFree)
    {
      group.top.addDependency( new InternalDimensionBrace( "CTBrace", child, group.vcenter, Side.vcenter, Side.top));
      group.bottom.addDependency( new InternalDimensionBrace( "CBBrace", child, group.vcenter, Side.vcenter, Side.bottom));
    }
    
    if ( isLeftFree && isRightFree && !isHCenterFree)
    {
      group.left.addDependency( new InternalDimensionBrace( "CLBrace", child, group.hcenter, Side.hcenter, Side.left));
      group.right.addDependency( new InternalDimensionBrace( "CRBrace", child, group.hcenter, Side.hcenter, Side.right));
    }
    
    //
    // Case 3: One edge and center are constrained
    //
    if ( !isTopFree && !isVCenterFree && isBottomFree)
    {
      group.bottom.addDependency( new InternalCenterBrace( "TCBBrace", child, group.top, Side.top, group.vcenter, Side.vcenter));
    }
    else if ( !isBottomFree && !isVCenterFree && isTopFree)
    {
      group.top.addDependency( new InternalCenterBrace( "BCTBrace", child, group.bottom, Side.bottom, group.vcenter, Side.vcenter));
    }
    
    if ( !isLeftFree && !isHCenterFree && isRightFree)
    {
      group.right.addDependency( new InternalCenterBrace( "LCRBrace", child, group.left, Side.left, group.hcenter, Side.hcenter));
    }
    else if ( !isRightFree && !isHCenterFree && isLeftFree)
    {
      group.left.addDependency( new InternalCenterBrace( "RCLBrace", child, group.left, Side.left, group.hcenter, Side.hcenter));
    }
    
    //
    // Case 4: Both edges are constrained, center is free
    //
    if ( !isTopFree && !isBottomFree && isVCenterFree)
    {
      group.vcenter.addDependency( new InternalCenterBrace( "TBCBrace", child, group.top, Side.top, group.bottom, Side.bottom));
    }
    
    if ( !isLeftFree && !isRightFree && isHCenterFree)
    {
      group.hcenter.addDependency( new InternalCenterBrace( "LRCBrace", child, group.left, Side.left, group.right, Side.right));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachContainer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  @Override
  public void attachContainer( IXidget xidget, Side side, int offset)
  {
    attachContainer( xidget, side, side, offset);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachContainer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachContainer( IXidget child, Side side1, Side side2, int offset)
  {
    IComputeNode node1 = getCreateNode( child, side1);
    IComputeNode node2 = getCreateNode( xidget, side2);
    if ( offset != 0) node1.addDependency( new OffsetNode( node2, offset)); else node1.addDependency( node2);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachContainer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, float, org.xmodel.IModelObject, int, boolean)
   */
  public void attachContainer( IXidget child, Side side, float percent, IModelObject percentNode, int offset, boolean handle)
  {
    IComputeNode node1 = getCreateNode( child, side);

    // get both sides of the container
    IComputeNode node2 = null;
    IComputeNode node3 = null;
    if ( side == Side.top || side == Side.bottom || side == Side.vcenter)
    {
      node2 = getCreateNode( xidget, Side.top);
      node3 = getCreateNode( xidget, Side.bottom);
    }
    else
    {
      node2 = getCreateNode( xidget, Side.left);
      node3 = getCreateNode( xidget, Side.right);
    }
    
    AnchorNode anchor = new AnchorNode( node2, node3, side, percent, percentNode, offset);
    anchor.setHandle( handle);
    node1.addDependency( anchor);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#packContainer(java.util.List, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void packContainer( List<IXidget> children, Side side, int offset)
  {
    if ( side == Side.left || side == Side.top) 
    {
      throw new IllegalArgumentException( 
        "Container may only be packed from the right or bottom sides.");
    }
    
    IComputeNode node1 = getCreateNode( xidget, side);
    
    if ( children.size() > 1)
    {
      IComputeNode node2 = new MaximumNode();
      for( IXidget child: children) node2.addDependency( getCreateNode( child, side));
      if ( offset != 0) node1.addDependency( new OffsetNode( node2, offset)); else node1.addDependency( node2);
    }
    else if ( children.size() > 0)
    {
      IComputeNode node2 = getCreateNode( children.get( 0), side);
      node1.addDependency( getOffsetNode( node2, offset));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPeer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.IXidget, int)
   */
  public void attachPeer( IXidget child, Side side, IXidget peer, int offset)
  {
    IComputeNode node1 = getCreateNode( child, side);
    IComputeNode node2 = getCreateNode( peer, getOpposite( side));
    node1.addDependency( getOffsetNode( node2, offset));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPeer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachPeer( IXidget child, Side fromSide, IXidget peer, Side toSide, int offset)
  {
    IComputeNode node1 = getCreateNode( child, fromSide);
    IComputeNode node2 = getCreateNode( peer, toSide);
    node1.addDependency( getOffsetNode( node2, offset));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPeers(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, java.util.List, org.xidget.ifeature.ILayoutFeature.Side, int, org.xidget.layout.IComputeNode)
   */
  @Override
  public void attachPeers( IXidget child, Side fromSide, List<IXidget> peers, Side toSide, int offset, IComputeNode node2)
  {
    IComputeNode node1 = getCreateNode( child, fromSide);
    node1.addDependency( getOffsetNode( node2, offset));
    
    for( IXidget peer: peers)
    {
      IComputeNode node3 = getCreateNode( peer, toSide);
      node2.addDependency( node3);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachNext(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachNext( IXidget child, Side fromSide, Side toSide, int offset)
  {
    List<IXidget> children = xidget.getChildren();
    int index = children.indexOf( child) + 1;
    if ( index < children.size())
    {
      attachPeer( child, fromSide, children.get( index), toSide, offset);
    }
    else
    {
      attachContainer( child, fromSide, toSide, offset);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPrevious(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachPrevious( IXidget child, Side fromSide, Side toSide, int offset)
  {
    List<IXidget> children = xidget.getChildren();
    int index = children.indexOf( child) - 1;
    if ( index >= 0)
    {
      attachPeer( child, fromSide, children.get( index), toSide, offset);
    }
    else
    {
      attachContainer( child, fromSide, toSide, offset);
    }
  }
  
  /**
   * Returns an instance of OffsetNode if offset is not zero, otherwise returns the node argument.
   * @param node The node from which the offset is calculated.
   * @param offset The offset.
   * @return Returns an instance of OffsetNode if offset is not zero, otherwise returns the node argument.
   */
  private final IComputeNode getOffsetNode( IComputeNode node, int offset)
  {
    if ( offset == 0) return node;
    return new OffsetNode( node, offset);
  }
  
  /**
   * Returns true if the specified node has no dependencies.
   * @param node The node to be tested.
   * @return Returns true if the specified node has no dependencies.
   */
  private boolean isNodeFree( IComputeNode node)
  {
    return node.getDependencies().size() == 0;
  }
  
  /**
   * Returns the opposite side.
   * @param side The side.
   * @return Returns the opposite side.
   */
  private Side getOpposite( Side side)
  {
    switch( side)
    {
      case top: return Side.bottom;
      case left: return Side.right;
      case right: return Side.left;
      case bottom: return Side.top;
    }
    return null;
  }
  
  /**
   * Returns (creates, if necessary) the NodeGroup for the specified xidget.
   * @param xidget The xidget.
   * @return Returns the NodeGroup for the specified xidget.
   */
  private NodeGroup getNodeGroup( IXidget xidget)
  {
    NodeGroup group = groups.get( xidget);
    if ( group == null)
    {
      group = new NodeGroup( xidget);
      groups.put( xidget, group);
    }
    return group;
  }
  
  /**
   * Returns the node for the specified side.
   * @param xidget The xidget.
   * @param side The side.
   * @return Returns the node for the specified side.
   */
  private IComputeNode getCreateNode( IXidget xidget, Side side)
  {
    NodeGroup group = getNodeGroup( xidget);
    switch( side)
    {
      case top: return group.top;
      case left: return group.left;
      case right: return group.right;
      case bottom: return group.bottom;
      case hcenter: return group.hcenter;
      case vcenter: return group.vcenter;
    }
    return null;
  }

  /**
   * Dependency sort the specified anchors. The algorithm must insure that when there
   * is a dependency cycle, the list of nodes in the cycle appears twice in the output
   * so that the layout can attempt to resolve it.
   * @param anchors The anchors.
   * @return Returns the sorted anchors.
   */
  private static List<IComputeNode> sort( Collection<IComputeNode> anchors)
  {
    List<IComputeNode> sorted = new ArrayList<IComputeNode>();
    Set<IComputeNode> consumed = new HashSet<IComputeNode>();
    for( IComputeNode anchor: anchors)
    {
      if ( consumed.contains( anchor)) continue;

      Stack<IComputeNode> stack = new Stack<IComputeNode>();
      stack.push( anchor);
      while( !stack.empty())
      {
        IComputeNode current = stack.peek();
        
        boolean found = false;
        for( IComputeNode depend: current.getDependencies())
        {
          if ( !consumed.contains( depend))
          {
            found = true;
            stack.push( depend);
            consumed.add( depend);
          }
        }
        
        if ( !found)
        {
          sorted.add( current);
          stack.pop();
        }
      }
    }

    return sorted;
  }

  /**
   * Create a default layout.
   */
  private void createDefaultLayout()
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = widgetFeature.getComputedBounds();

    IWidgetContainerFeature containerFeature = xidget.getFeature( IWidgetContainerFeature.class);
    int spacing = containerFeature.getSpacing();
    
    List<IXidget> children = xidget.getChildren();
    if ( children.size() == 0) return;

    List<Bounds> childrenBounds = new ArrayList<Bounds>();
    for( IXidget child: children) 
    {
      childrenBounds.add( child.getFeature( IWidgetFeature.class).getDefaultBounds());
    }
    
    // attach top of first component to container
    attachContainer( children.get( 0), Side.top, Side.top, 0);

    // attach top of each widget to bottom of previous
    for( int i=1; i<children.size(); i++)
    {
      attachPeer( children.get( i), Side.top, children.get( i-1), Side.bottom, spacing);
    }
    
    // attach left of each component to container
    for( IXidget child: children) 
    {
      attachContainer( child, Side.left, Side.left, 0);
    }
    
    // attach right of each component to container, if applicable
    if ( bounds.isWidthDefined())
    {
      for( int i=0; i<children.size(); i++)
      {
        if ( !childrenBounds.get( i).isWidthDefined())
          attachContainer( children.get( i), Side.right, Side.right, 0);
      }
    }
    
    // attach bottom of each component to container, if applicable
    if ( bounds.isHeightDefined())
    {
      float remainingHeight = bounds.height;
      int nConstHeight = 0;
      for( Bounds childBounds: childrenBounds)
      {
        if ( childBounds.isHeightDefined()) 
        {
          remainingHeight -= childBounds.height;
          nConstHeight++;
        }
      }
      
      float y = 0;
      float freeHeight = (nConstHeight < children.size())? remainingHeight / nConstHeight: 0;
      for( int i=0; i<children.size(); i++)
      {
        if ( childrenBounds.get( i).isHeightDefined())
        {
          y += childrenBounds.get( i).height;
        }
        else
        {
          float percent = (y + freeHeight) / bounds.height;
          attachContainer( children.get( i), Side.bottom, percent, null, 0, false);
        }
      }
    }
  }
  
  private void logLayout()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "LAYOUT: "); sb.append( xidget); sb.append( '\n');
    
    for( IComputeNode node: sorted)
    {
      if ( node instanceof WidgetHandle) continue;
      if ( node instanceof InternalDimensionBrace) continue;
      if ( node instanceof InternalCenterBrace) continue;
      logNode( node, sb);
    }

    List<IXidget> list = new ArrayList<IXidget>();
    list.add( xidget);
    list.addAll( xidget.getChildren());
    
    for( IXidget xidget: list)
    {
      NodeGroup group = groups.get( xidget);
      
      sb.append( "\n");
      sb.append( xidget);
      sb.append( "\n");

      logNode( group.top, sb);
      logNode( group.bottom, sb);
      logNode( group.left, sb);
      logNode( group.right, sb);
      logNode( group.vcenter, sb);
      logNode( group.hcenter, sb);
    }
    
    log.debug( sb.toString());
  }

  private void logNode( IComputeNode node, StringBuilder sb)
  {
    for( IComputeNode depend: node.getDependencies())
    {
      if ( depend instanceof InternalDimensionBrace || depend instanceof InternalCenterBrace)
        logNode( depend, sb);
    }
    
    sb.append( "\t");
    sb.append( node);
    sb.append( '\n');
  }
  
  private final static class NodeGroup
  {
    public NodeGroup( IXidget xidget)
    {
      top = new WidgetHandle( xidget, Side.top, 0);
      left = new WidgetHandle( xidget, Side.left, 0);
      right = new WidgetHandle( xidget, Side.right, 0);
      bottom = new WidgetHandle( xidget, Side.bottom, 0);
      hcenter = new WidgetHandle( xidget, Side.hcenter, 0);
      vcenter = new WidgetHandle( xidget, Side.vcenter, 0);
    }
    
    public IComputeNode top;
    public IComputeNode left;
    public IComputeNode right;
    public IComputeNode bottom;
    public IComputeNode hcenter;
    public IComputeNode vcenter;
  }

  private final static Log log = Log.getLog( AnchorLayoutFeature.class);
  
  private IXidget xidget;
  private ScriptAction script;
  private Map<IXidget, NodeGroup> groups;
  private List<IComputeNode> sorted;
  private float width;
  private float height;
}
