/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
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
import org.xidget.Log;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.layout.AnchorNode;
import org.xidget.layout.Bounds;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.Margins;
import org.xidget.layout.MaxNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.Size;
import org.xidget.layout.WidgetHandle;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
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
    this.spacing = 2;
    invalidate();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#invalidate()
   */
  public void invalidate()
  {
    sorted = null;
    groups.clear();
    createContainerNodes();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#layout(org.xmodel.xpath.expression.StatefulContext)
   */
  public void layout( StatefulContext context)
  {
    // set container right and bottom nodes based on container size
    initContainerSize();

    // compile layout
    if ( sorted == null) 
    {
      compile( context);
    }
    
    // calculate layout
    if ( sorted != null)
    {
      for( IComputeNode node: sorted) node.reset();
      for( IComputeNode node: sorted) node.update();
      for( IComputeNode node: sorted) node.update();
    }
    
    // update bounds of children
    updateChildrenBounds();
    
    // update container bounds
    updateContainerSize();
  }
  
  /**
   * Initialize the right and bottom nodes of the container.
   */
  private void initContainerSize()
  {
    Margins margins = getMargins();
    
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    if ( bounds.width > 0)
    {
      IComputeNode right = getCreateNode( xidget, Side.right);
      right.setDefaultValue( bounds.width - margins.x0 - margins.x1);
    }
    
    if ( bounds.height > 0)
    {
      IComputeNode bottom = getCreateNode( xidget, Side.bottom);
      bottom.setDefaultValue( bounds.height - margins.y0 - margins.y1);
    }
  }
  
  /**
   * Update the bounds of each child based on the value of its nodes.
   */
  private void updateChildrenBounds()
  {
    Margins margins = getMargins();
    Bounds bounds = new Bounds();
    for( Map.Entry<IXidget, NodeGroup> entry: groups.entrySet())
    {
      IXidget xidget = entry.getKey();
      NodeGroup group = entry.getValue();
      
      if ( xidget == this.xidget) continue;
      
      bounds.x = 0;
      bounds.y = 0;
      bounds.width = 0;
      bounds.height = 0;
      
      IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
      if ( group.top != null && group.top.hasValue()) bounds.y = group.top.getValue() + margins.y0;
      if ( group.left != null && group.left.hasValue()) bounds.x = group.left.getValue() + margins.x0;
      
      if ( group.right != null && group.right.hasValue()) 
      {
        if ( group.left == null || !group.left.hasValue()) Log.printf( "layout", "Width of child not constrained: %s\n", xidget);
        bounds.width = group.right.getValue() - group.left.getValue();
      }
      
      if ( group.bottom != null && group.bottom.hasValue()) 
      {
        if ( group.top == null || !group.top.hasValue()) Log.printf( "layout", "Height of child not constrained: %s\n", xidget);
        bounds.height = group.bottom.getValue() - group.top.getValue();
      }
      
      widgetFeature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
    }
  }
  
  /**
   * Update the size of the container based on its right and bottom nodes.
   */
  private void updateContainerSize()
  {
    IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
    Bounds bounds = new Bounds(); widgetFeature.getBounds( bounds);
    
    IComputeNode right = getCreateNode( xidget, Side.right);
    IComputeNode bottom = getCreateNode( xidget, Side.bottom);

    Margins margins = getMargins();
    if ( right.hasValue()) bounds.width = right.getValue() + margins.x1;
    if ( bottom.hasValue()) bounds.height = bottom.getValue() + margins.y1;
    
    // set widget size
    widgetFeature.setBounds( bounds.x, bounds.y, bounds.width, bounds.height);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#setMargins(org.xidget.layout.Margins)
   */
  public void setMargins( Margins margins)
  {
    this.margins = margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#getMargins()
   */
  public Margins getMargins()
  {
    if ( margins == null)
    {
      if ( xidget.getParent() != null)
      {
        ILayoutFeature parentFeature = xidget.getParent().getFeature( ILayoutFeature.class);
        if ( parentFeature != null) return parentFeature.getMargins();
      }
    }
    
    if ( margins == null) 
    {
      margins = new Margins( 2, 2, 2, 2);
    }
    
    return margins;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#setSpacing(int)
   */
  public void setSpacing( int spacing)
  {
    this.spacing = spacing;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#getSpacing()
   */
  public int getSpacing()
  {
    if ( margins == null)
    {
      ILayoutFeature parentFeature = xidget.getParent().getFeature( ILayoutFeature.class);
      if ( parentFeature != null) return parentFeature.getSpacing();
    }
    
    return spacing;
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
   * @param context The widget context.
   */
  private void compile( StatefulContext context)
  {
    Log.printf( "layout", "Compile: %s\n", xidget);

    if ( script == null) loadScript( context);
      
    // execute script
    if ( script != null)
    {
      StatefulContext scriptContext = new StatefulContext( context, xidget.getConfig());
      script.run( scriptContext);
    }
    
    // add internal bracing nodes
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
    }
    
    // sort nodes
    sorted = sort( nodes);
    
    // dump final node list
    for( int i=0; i<sorted.size(); i++)
      Log.printf( "layout", "%d: %s\n", i, sorted.get( i));
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
        XActionDocument document = new XActionDocument( element);
        ClassLoader loader = getClass().getClassLoader();
        document.setClassLoader( loader);
        script = document.createScript();
      }
    }
    
    // load script in layout child
    else
    {
      IModelObject layout = config.getFirstChild( "layout");
      if ( layout != null)
      {
        XActionDocument document = new XActionDocument( layout);
        ClassLoader loader = getClass().getClassLoader();
        document.setClassLoader( loader);
        script = document.createScript();
      }
    }
  }
  
  /**
   * Create the container constant nodes.
   */
  private void createContainerNodes()
  {
    NodeGroup group = getNodeGroup( xidget);
    
    group.top = new WidgetHandle( "ctop", 0);
    group.left = new WidgetHandle( "cleft", 0);
    group.right = new WidgetHandle( "cright", 0);
    group.bottom = new WidgetHandle( "cbottom", 0);
    
    Margins margins = getMargins();
    group.top.setDefaultValue( (float)margins.y0);
    group.left.setDefaultValue( (float)margins.x0);
  }
  
  /**
   * Add internal braces to the specified node if one of its sides is not defined.
   * @param xidget The xidget.
   */
  private void addInternalBraces( IXidget child)
  {
    IWidgetFeature widgetFeature = child.getFeature( IWidgetFeature.class);
    Size size = new Size(); widgetFeature.getPreferredSize( size);
    
    NodeGroup group = getNodeGroup( child);
    if ( group.top == null)
    {
      if ( group.bottom != null) group.top = new OffsetNode( group.bottom, -size.height);
    }
    else if ( group.bottom == null)
    {
      if ( group.top != null) group.bottom = new OffsetNode( group.top, size.height);
    }
    else if ( group.left == null)
    {
      if ( group.right != null) group.left = new OffsetNode( group.right, -size.width);
    }
    else if ( group.right == null)
    {
      if ( group.left != null) group.right = new OffsetNode( group.left, size.width);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachContainer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachContainer( IXidget child, Side side, int offset)
  {
    IComputeNode node1 = getCreateNode( child, side);
    IComputeNode node2 = getCreateNode( xidget, side);
    node1.addDependency( new OffsetNode( node2, offset));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachContainer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, float, int, boolean)
   */
  public void attachContainer( IXidget child, Side side, float percent, int offset, boolean handle)
  {
    IComputeNode node1 = getCreateNode( child, side);

    // get both sides of the container
    IComputeNode node2 = null;
    IComputeNode node3 = null;
    if ( side == Side.top || side == Side.bottom)
    {
      node2 = getCreateNode( xidget, Side.top);
      node3 = getCreateNode( xidget, Side.bottom);
    }
    else
    {
      node2 = getCreateNode( xidget, Side.left);
      node3 = getCreateNode( xidget, Side.right);
    }
    
    AnchorNode anchor = new AnchorNode( node2, node3, side, percent, offset);
    anchor.setHandle( handle);
    node1.addDependency( anchor);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#packContainer(java.util.List, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void packContainer( List<IXidget> children, Side side, int offset)
  {
    if ( side == Side.left || side == Side.top) return;
    
    Margins margins = getMargins();
    int margin = 0;
    switch( side)
    {
      case right: margin = margins.x1; break;
      case bottom: margin = margins.y1; break;
    }
    
    IComputeNode node1 = getCreateNode( xidget, side);
    
    if ( children.size() > 1)
    {
      IComputeNode node2 = new MaxNode();
      for( IXidget child: children) node2.addDependency( getCreateNode( child, side));
      node1.addDependency( new OffsetNode( node2, margin + offset));
    }
    else
    {
      IComputeNode node2 = getCreateNode( children.get( 0), side);
      node1.addDependency( new OffsetNode( node2, margin + offset));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPeer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.IXidget, int)
   */
  public void attachPeer( IXidget child, Side side, IXidget peer, int offset)
  {
    IComputeNode node1 = getCreateNode( child, side);
    IComputeNode node2 = getCreateNode( peer, getOpposite( side));
    node1.addDependency( new OffsetNode( node2, offset));
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#attachPeer(org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, org.xidget.IXidget, org.xidget.ifeature.ILayoutFeature.Side, int)
   */
  public void attachPeer( IXidget child, Side fromSide, IXidget peer, Side toSide, int offset)
  {
    IComputeNode node1 = getCreateNode( child, fromSide);
    IComputeNode node2 = getCreateNode( peer, toSide);
    node1.addDependency( new OffsetNode( node2, offset));
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
      attachContainer( child, fromSide, offset);
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
      attachContainer( child, fromSide, offset);
    }
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
      group = new NodeGroup();
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
      case top:
        if ( group.top == null) group.top = new WidgetHandle( "top", 0);
        return group.top;
        
      case left: 
        if ( group.left == null) group.left = new WidgetHandle( "left", 0);
        return group.left;
        
      case right: 
        if ( group.right == null) group.right = new WidgetHandle( "right", 0);
        return group.right;
        
      case bottom: 
        if ( group.bottom == null) group.bottom = new WidgetHandle( "bottom", 0);
        return group.bottom;
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
  
  private class NodeGroup
  {
    IComputeNode top;
    IComputeNode left;
    IComputeNode right;
    IComputeNode bottom;
  }

  private IXidget xidget;
  private ScriptAction script;
  private Map<IXidget, NodeGroup> groups;
  private List<IComputeNode> sorted;
  private Margins margins;
  private int spacing;
}
