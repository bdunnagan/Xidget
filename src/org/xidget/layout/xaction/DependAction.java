/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.IComputeNodeFeature;
import org.xidget.ifeature.IComputeNodeFeature.Type;
import org.xidget.layout.ConstantNode;
import org.xidget.layout.IComputeNode;
import org.xidget.layout.OffsetNode;
import org.xidget.layout.ProportionalNode;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An action that creates one or more dependencies between xidget anchors.
 */
public class DependAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
   
    List<IModelObject> nodes = document.getRoot().getChildren( "node");
    
    sourceExpr = document.getExpression( nodes.get( 0));
    sourceType = Xlate.get( nodes.get( 0), "type", (String)null);
    
    dependExpr = document.getExpression( nodes.get( 1));
    dependType = Xlate.get( nodes.get( 1), "type", (String)null);
    
    percentExpr = document.getExpression( "percent", true);
    offsetExpr = document.getExpression( "offset", true);
    constantExpr = document.getExpression( "constant", true);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    List<IModelObject> inElements = sourceExpr.query( context, null);
    List<IModelObject> outElements = dependExpr.query( context, null);
  
    for( IModelObject inElement: inElements)
    {
      for( IModelObject outElement: outElements)
      {
        IXidget source = (IXidget)inElement.getAttribute( "xidget");
        if ( source == null) throw new XActionException( "First xidget not found: "+inElement);
        
        IXidget depend = (IXidget)outElement.getAttribute( "xidget");
        if ( depend == null) throw new XActionException( "Second xidget not found: "+outElement);
  
        IComputeNode sourceNode = getComputeNode( source, source == parent, Type.valueOf( sourceType));
        IComputeNode dependNode = getComputeNode( depend, depend == parent, Type.valueOf( dependType));
        
        if ( offsetExpr != null && percentExpr == null)
        {
          double offset = offsetExpr.evaluateNumber( context);
          dependNode = new OffsetNode( dependNode, (int)offset);
        }
        else if ( percentExpr != null)
        {
          double offset = (offsetExpr != null)? offsetExpr.evaluateNumber( context): 0;
          double percent = percentExpr.evaluateNumber( context);
          dependNode = new ProportionalNode( dependNode, (float)percent, (int)offset);
        }
        else if ( constantExpr != null)
        {
          double constant = constantExpr.evaluateNumber( context);
          dependNode = new ConstantNode( (int)constant);
        }
        
        sourceNode.addDependency( dependNode);
      }
    }
  }     
  
  /**
   * Returns the IComputeNode of the specified type for the specified xidget.
   * If the parent flag is true, then the xidget is the parent container.
   * @param xidget The xidget.
   * @param parent True if xidget is parent container.
   * @param type The type of IComputeNode.
   * @return Returns the IComputeNode of the specified type for the specified xidget.
   */
  private IComputeNode getComputeNode( IXidget xidget, boolean parent, Type type)
  {
    IComputeNodeFeature feature = xidget.getFeature( IComputeNodeFeature.class);
    return parent? feature.getParentAnchor( type): feature.getAnchor( type);
  }
    
  private IExpression sourceExpr;
  private IExpression dependExpr;
  private String sourceType;
  private String dependType;
  private IExpression percentExpr;
  private IExpression offsetExpr;
  private IExpression constantExpr;
}
