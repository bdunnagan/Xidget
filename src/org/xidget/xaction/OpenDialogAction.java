/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.xaction;

import java.util.List;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.config.TagException;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.dialog.IDialogFeature;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.ScriptAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An XAction that opens a dialog.
 */
public class OpenDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  @Override
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    dialogExpr = document.getExpression( "config", true);
    contextExpr = document.getExpression( "context", true);
    onClose = document.createChildScript( "onClose");
  }

  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected Object[] doAction( IContext context)
  {
    IModelObject config = dialogExpr.queryFirst( context);
    if ( config == null) throw new XActionException( "Unable to open dialog because configuration not found: "+dialogExpr);

    IXidget xidget = null;
    
    try
    {
      List<IXidget> xidgets = Creator.getInstance().create( new StatefulContext( context, config), false);
      xidget = xidgets.get( 0);
    }
    catch( TagException e)
    {
      throw new XActionException( "Unable to create dialog: "+config, e);
    }
        
    // find dialog context
    IModelObject element = (contextExpr != null)? contextExpr.queryFirst( context): context.getObject();
    if ( element == null) return null;
      
    StatefulContext dialogContext = new StatefulContext( context, element);
    
    // set xidget in dialog context
    IModelObject holder = new ModelObject( "xidget");
    holder.setValue( xidget);
    dialogContext.set( "dialog", holder);
    
    // bind dialog
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.bind( dialogContext);
    
    // open dialog
    try
    {
      IDialogFeature dialogFeature = xidget.getFeature( IDialogFeature.class);
      dialogFeature.open( dialogContext);
      if ( onClose != null) return onClose.run( dialogContext);
    }
    finally
    {
      // unbind dialog
      bindFeature.unbind( dialogContext);
      
      // destory
      Creator.getInstance().destroy( xidget);
    }
    
    return null;
  }
  
  private IExpression dialogExpr;
  private IExpression contextExpr;
  private ScriptAction onClose;
}
