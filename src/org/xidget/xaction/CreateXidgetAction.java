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
    sourceExpr = document.getExpression();
    variable = Xlate.get( document.getRoot(), "assign", (String)null);
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void doAction( IContext context)
  {
    IModelObject root = sourceExpr.queryFirst( context);
    if ( root == null) throw new XActionException( "Xidget not found at: "+sourceExpr);
    
    try
    {
      StatefulContext inner = new StatefulContext( context, root);
      Creator creator = Creator.getInstance();
      List<IXidget> xidgets = creator.create( inner, true);
      
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
  }
  
  private IExpression sourceExpr;
  private String variable;
}
