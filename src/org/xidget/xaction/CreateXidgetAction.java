package org.xidget.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.ifeature.IWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableScope;

/**
 * An XAction which loads the xidget configuration specified by an xpath.
 */
public class CreateXidgetAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    contextExpr = document.getExpression( "context", true);
    parentExpr = document.getExpression( "parent", true);
    xidgetExpr = document.getExpression( "xidget", true);
    if ( xidgetExpr == null) xidgetExpr = document.getExpression();
    variable = Xlate.get( document.getRoot(), "assign", (String)null);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject root = xidgetExpr.queryFirst( context);
    if ( root == null) throw new XActionException( "Xidget not found at: "+xidgetExpr);

    try
    {
      StatefulContext configContext = new StatefulContext( context, root);
      initVariables( configContext);
      
      StatefulContext bindContext = createBindContext( context, configContext);
      IModelObject parentNode = (parentExpr != null)? parentExpr.queryFirst( context): null;
      IXidget parent = Xlate.get( parentNode, "instance", (IXidget)null);
      List<IXidget> xidgets = Creator.getInstance().create( parent, configContext, bindContext);
      
      if ( variable != null)
      {
        List<IModelObject> holders = new ArrayList<IModelObject>( 1);
        for( IXidget xidget: xidgets)
        {
          IModelObject holder = new ModelObject( "xidget");
          holder.setValue( xidget);
          holders.add( holder);
        }
        
        IVariableScope scope = context.getScope();
        if ( scope != null) scope.set( variable, holders);
      }
      
      for( IXidget xidget: xidgets)
      {
        IWidgetFeature widgetFeature = xidget.getFeature( IWidgetFeature.class);
        if ( widgetFeature != null) widgetFeature.setVisible( true);
      }
    }
    catch( TagException e)
    {
      throw new XActionException( e);
    }
    
    return null;
  }

  /**
   * Create the context in which the xidget will be bound. The configuration context will be its parent.
   * @param context The xaction context.
   * @param configContext The configuration context.
   * @return Returns the new binding context.
   */
  private StatefulContext createBindContext( IContext context, StatefulContext configContext)
  {
    if ( contextExpr != null)
    {
      IModelObject node = contextExpr.queryFirst( context);
      if ( node != null) return new StatefulContext( configContext, node);
    }
    return configContext;
  }
  
  /**
   * Initialize global variables on the specified context.
   * @param context The context.
   */
  private void initVariables( StatefulContext context)
  {
    if ( context.get( "org.xidget.ui") == null)
      context.set( "org.xidget.ui", new ModelObject( "org.xidget.ui"));
  }
  
  private IExpression contextExpr;
  private IExpression parentExpr;
  private IExpression xidgetExpr;
  private String variable;
}
