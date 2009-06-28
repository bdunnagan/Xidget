/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.ProportionalNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.XPath;
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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#configure()
   */
  public void configure()
  {
    sorted = null;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#layout(org.xmodel.xpath.expression.StatefulContext)
   */
  public void layout( StatefulContext context)
  {
    if ( sorted == null) compile( context);
    
    Log.printf( "layout", "Layout: %s\n", xidget);
    
    if ( sorted != null)
    {
      for( IComputeNode node: sorted) 
        node.update();
    }
    
    Log.println( "layout", "");
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#addNode(org.xidget.layout.IComputeNode)
   */
  public void addNode( IComputeNode node)
  {
    Log.printf( "layout", "Added: %s\n", node);
    
    // a node can only appear in the list once but the node dependencies may have changed
    sorted = null;
    if ( nodes == null) nodes = new ArrayList<IComputeNode>();
    if ( !nodes.contains( node)) nodes.add( node);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#removeNode(org.xidget.layout.IComputeNode)
   */
  public void removeNode( IComputeNode node)
  {
    Log.printf( "layout", "Added: %s\n", node);
    sorted = null;
    if ( nodes != null) nodes.remove( node);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#clearNodes()
   */
  public void clearNodes()
  {
    sorted = null;
    if ( nodes != null) nodes.clear();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#getNodes()
   */
  public List<IComputeNode> getNodes()
  {
    return nodes;
  }

  /**
   * Execute the layout script and sort the computation nodes.
   * @param context The widget context.
   */
  private void compile( StatefulContext context)
  {
    Log.printf( "layout", "Compile: %s\n", xidget);

    IModelObject config = xidget.getConfig();
    
    // get layout script element
    IModelObject layout = config.getAttributeNode( "layout");
    if ( layout == null) layout = config.getFirstChild( "layout");
    
    // bail if no layout
    if ( layout == null) return;
    
    // get attachment declarations
    List<IModelObject> attachments = layout.getChildren( "attachment");

    // process attachments
    if ( attachments.size() > 0)
    {
      for( IModelObject attachment: attachments)
        processAttachment( attachment);
    }
    
    // execute layout script if defined
    else
    {
      IExpression layoutExpr = Xlate.childGet( layout, "script", Xlate.get( layout, (IExpression)null));
      IModelObject element = layoutExpr.queryFirst( context);
      
      XActionDocument document = new XActionDocument( element);
      ClassLoader loader = getClass().getClassLoader();
      document.setClassLoader( loader);
      ScriptAction script = document.createScript();
      
      StatefulContext scriptContext = new StatefulContext( context, xidget.getConfig());
      script.run( scriptContext);
    }
    
    // create final node list
    if ( nodes != null)
    {
      // sort nodes
      sorted = sort( nodes);
      
      // dump final node list
      for( int i=0; i<sorted.size(); i++)
        Log.printf( "layout", "%d: %s\n", i, sorted.get( i));
    }
  }
  
  /**
   * Add IComputeNodes according to the specified attachment declaration.
   * @param attachment The attachment declaration.
   */
  private void processAttachment( IModelObject attachment)
  {
    IModelObject config = xidget.getConfig();
    StatefulContext context = new StatefulContext( config);
    
    IExpression toExpr = Xlate.get( attachment, "to", (IExpression)containerExpr);
    if ( toExpr == null)
    {
      Log.printf( "layout", "Missing 'to' expression in attachment declaration: %s\n", xidget);
      return;
    }
    
    IModelObject toNode = toExpr.queryFirst( context);
    if ( toNode == null)
    {
      Log.printf( "layout", "Empty set returned by 'to' expression in attachment declaration: %s\n", xidget);
      return;
    }

    IXidget toXidget = (IXidget)toNode.getAttribute( "xidget");
    if ( toXidget == null)
    {
      Log.printf( "layout", "Node returned by 'to' expression in attachment declaration is not a xidget: %s\n", toNode);
      return;
    }
    
    IComputeNode fromComputeNode = getComputeNode( xidget, Type.valueOf( attachment.getType()));
    
    // clear dependencies
    fromComputeNode.clearDependencies();
    
    // make sure this computation node is part of the layout
    addNode( fromComputeNode);

    // create intermediate computation nodes
    IExpression constantExpr = Xlate.get( attachment, "constant", (IExpression)null);
    IExpression offsetExpr = Xlate.get( attachment, "offset", (IExpression)null);
    IExpression percentExpr = Xlate.get( attachment, "percent", (IExpression)null);

    if ( constantExpr != null)
    {
      double constant = constantExpr.evaluateNumber( context);
      fromComputeNode.addDependency( new ConstantNode( (int)Math.round( constant)));
    }
    else
    {
      IComputeNode toComputeNode = getComputeNode( toXidget, Type.valueOf( Xlate.get( attachment, "side", attachment.getType())));
      
      int offset = (int)Math.round( (offsetExpr != null)? offsetExpr.evaluateNumber( context): 0);
      if ( percentExpr != null)
      {
        float percent = (float)percentExpr.evaluateNumber( context);
        ProportionalNode percentNode = new ProportionalNode( toComputeNode, percent, offset);
        fromComputeNode.addDependency( percentNode);
      }
      else if ( offset > 0)
      {
        OffsetNode offsetNode = new OffsetNode( toComputeNode, offset);
        fromComputeNode.addDependency( offsetNode);
      }
      else
      {
        fromComputeNode.addDependency( toComputeNode);
      }
    }
  }
  
  /**
   * Returns the specified IComputeNode from the specified xidget.
   * @param xidget The xidget.
   * @param type The type of node.
   * @return Returns the specified IComputeNode from the specified xidget.
   */
  public static IComputeNode getComputeNode( IXidget xidget, Type type)
  {
    IComputeNodeFeature feature = xidget.getFeature( IComputeNodeFeature.class);
    return feature.getComputeNode( type);
  }
  
  /**
   * Dependency sort the specified anchors.
   * @param anchors The anchors.
   * @return Returns the sorted anchors.
   */
  private static List<IComputeNode> sort( List<IComputeNode> anchors)
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
          if ( !consumed.contains( depend))
          {
            found = true;
            stack.push( depend);
            consumed.add( depend);
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

  private final IExpression containerExpr = XPath.createExpression( "..");
  
  private IXidget xidget;
  private List<IComputeNode> nodes;
  private List<IComputeNode> sorted;
}
