/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import org.xidget.IXidget;
import org.xidget.config.XidgetMap;
import org.xidget.config.processor.TagProcessor;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
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
  public AnchorLayoutFeature()
  {
    mapHolder = new ModelObject( "holder");
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#layout()
   */
  public void layout()
  {
    if ( !sorted) 
    {
      nodes = sort( nodes);
      sorted = true;
    }
    
    for( IComputeNode anchor: nodes)
      anchor.update();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.layout.ILayoutFeature#setLayout(org.xidget.config.processor.TagProcessor, org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public void setLayout( TagProcessor processor, IXidget xidget, IModelObject element)
  {
    sorted = false;
    
    XActionDocument document = new XActionDocument( element);
    document.setClassLoader( getClass().getClassLoader());
    ScriptAction script = document.createScript();
    
    XidgetMap map = processor.getFeature( XidgetMap.class);
    StatefulContext context = new StatefulContext( map.getConfig( xidget));
    mapHolder.setValue( map);
    context.set( "map", mapHolder);
    script.run( context);
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
      while( !stack.empty())
      {
        IComputeNode current = stack.peek();
        
        boolean found = false;
        for( IComputeNode depend: current.getDependencies())
          if ( !consumed.contains( depend))
          {
            found = true;
            stack.push( depend);
          }
        
        if ( !found)
        {
          sorted.add( current);
          consumed.add( current);
          stack.pop();
        }
      }
    }
    return sorted;
  }
  
  private List<IComputeNode> nodes;
  private IModelObject mapHolder;
  private boolean sorted;
}
