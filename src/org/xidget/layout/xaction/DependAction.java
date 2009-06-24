/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.layout.xaction;

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
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of IXAction that creates a dependency between any two xidget anchors,
 * including dependencies between a container and its children.
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
   
    IModelObject sourceNode = document.getRoot().getFirstChild( "source");
    sourceExpr = document.getExpression( sourceNode);
    sourceType = Xlate.get( sourceNode, "type", (String)null);
    
    IModelObject dependNode = document.getRoot().getFirstChild( "depend");
    dependExpr = document.getExpression( dependNode);
    dependType = Xlate.get( dependNode, "type", (String)null);
    
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
    IModelObject parentConfig = context.getObject();
    IXidget parent = (IXidget)parentConfig.getAttribute( "xidget");
    ILayoutFeature layout = parent.getFeature( ILayoutFeature.class);
    
    List<IModelObject> inElements = sourceExpr.query( context, null);
    List<IModelObject> outElements = (dependExpr != null)? dependExpr.query( context, null): null;
    
    for( IModelObject inElement: inElements)
    {
      IXidget source = (IXidget)inElement.getAttribute( "xidget");
      if ( source == null) throw new XActionException( "First xidget not found: "+inElement);
      
      IComputeNode sourceNode = getComputeNode( source, source == parent, Type.valueOf( sourceType));
      
      if ( constantExpr == null)
      {
        for( IModelObject outElement: outElements)
        {
          IXidget depend = (IXidget)outElement.getAttribute( "xidget");
          if ( depend == null) throw new XActionException( "Second xidget not found: "+outElement);
    
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
          
          sourceNode.addDependency( dependNode);
        }
      }
      else
      {
        double constant = constantExpr.evaluateNumber( context);
        sourceNode.addDependency( new ConstantNode( (int)constant));
      }
      
      layout.addNode( sourceNode);
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
