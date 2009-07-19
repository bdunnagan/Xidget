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
import org.xidget.layout.IComputeNode;
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
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ILayoutFeature#configure()
   */
  public void configure()
  {
    //TODO: rename this method???
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
      for( IComputeNode node: sorted) node.reset();
      
      //
      // Two passes gives circular dependencies a chance to resolve.
      // This could also be accomplished with a better sorting algorithm.
      //
      for( IComputeNode node: sorted) node.update();
      for( IComputeNode node: sorted) node.update();
    }
    
    Log.println( "layout", "");
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

    IModelObject config = xidget.getConfig();
    
    // load script pointed to by layout attribute
    ScriptAction script = null;
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
        IModelObject scriptNode = layout.getFirstChild( "script");
        if ( scriptNode != null)
        {
          XActionDocument document = new XActionDocument( scriptNode);
          ClassLoader loader = getClass().getClassLoader();
          document.setClassLoader( loader);
          script = document.createScript();
        }
      }
    }
    
    // execute script
    if ( script != null)
    {
      StatefulContext scriptContext = new StatefulContext( context, xidget.getConfig());
      script.run( scriptContext);
    }
    
    // add container nodes to the layout
    nodes = new ArrayList<IComputeNode>();
    IComputeNodeFeature computeNodeFeature = xidget.getFeature( IComputeNodeFeature.class);
    nodes.addAll( computeNodeFeature.getAccessedList());
    
    // add nodes for children
    for( IXidget child: xidget.getChildren())
    {
      computeNodeFeature = child.getFeature( IComputeNodeFeature.class);
      nodes.addAll( computeNodeFeature.getAccessedList());
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

  private IXidget xidget;
  private List<IComputeNode> nodes;
  private List<IComputeNode> sorted;
}
