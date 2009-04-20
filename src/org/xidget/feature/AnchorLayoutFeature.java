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
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.ILayoutFeature;
import org.xidget.layout.IComputeNode;
import org.xmodel.IModelObject;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A layout algorithm where the layout contraints are specified as attachments (joints) between
 * xidgets. This layout algorithm is similar to the SWT FormLayout or Swing SpringLayout, but
 * provides consistent behavior across platforms.
 * <p>
 * <code>
 * <layout name="l1">
 *   [<size>width, height</size>]
 *   [<min>width, height</min>]
 *   [<max>width, height</max>]
 *   <x0 attach="xpath" side="[x0|x1|y0|y1]" pad="N" anchor="%N"/>
 *   <y0 attach="xpath" side="[x0|x1|y0|y1]" pad="N" anchor="%N"/>
 *   <x1 attach="xpath" side="[x0|x1|y0|y1]" pad="N" anchor="%N"/>
 *   <y1 attach="xpath" side="[x0|x1|y0|y1]" pad="N" anchor="%N"/>
 * </layout>
 * <p>
 * The xpath argument is relative to the xidget in which the layout element appears.
 * Therefore, the following layout could be used for a vertical layout:
 * <p>
 * <layout name="vertical">
 *   <x0 attach=".." side="left" pad="5"/>
 *   <y0 attach="preceding::*[1]" side="bottom" pad="3"/>
 *   <y0 attach=".." side="top" pad="5"/>
 *   <x1 attach=".." side="right" pad="5"/>
 * </layout>
 * <p>
 * When there is more than one declaration for a particular side, the first declaration
 * whose xpath expression is not an empty set will be used. This allows for a more 
 * compact representation of some layouts.
 * </code>
 */
public class AnchorLayoutFeature implements ILayoutFeature
{
  public AnchorLayoutFeature( IXidget container)
  {
    this.xidget = container;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#layout()
   */
  public void layout()
  {
    if ( !compiled) compile(); 
    
    Log.printf( "layout", "\nCompute Graph --------------\n");
    for( IComputeNode anchor: nodes) anchor.update();
    Log.printf( "layout", "\nDone -----------------------\n");
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#setLayout(org.xidget.config.processor.TagProcessor, org.xmodel.IModelObject)
   */
  public void configure( TagProcessor processor, IModelObject element)
  {
    compiled = false;
    config = element;
  }
   
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#addNode(org.xidget.layout.IComputeNode)
   */
  public void addNode( IComputeNode node)
  {
    nodes.add( node);
  }

  /**
   * Execute the layout script and sort the computation nodes.
   */
  private void compile()
  {
    if ( config == null) throw new RuntimeException( "Layout not defined for xidget: "+xidget);
    
    compiled = true;
    nodes = new ArrayList<IComputeNode>();

    // run layout script
    XActionDocument document = new XActionDocument( config);
    document.setClassLoader( getClass().getClassLoader());
    ScriptAction script = document.createScript();
    
    StatefulContext context = new StatefulContext( xidget.getConfig());
    script.run( context);

    // sort nodes
    nodes = sort( nodes);
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

  private IXidget xidget;
  private IModelObject config;
  private List<IComputeNode> nodes;
  private boolean compiled;
}
