/*
 * Xidget - XML Widgets based on JAHM
 * 
 * OpenDialogAction.java
 * 
 * Copyright 2009 Robert Arvin Dunnagan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * An XAction that opens a dialog xidget given an xpath identifying the configuration and an xpath
 * that returns the context node for the dialog. A script is executed when the dialog closes.
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
      List<IXidget> xidgets = Creator.getInstance().create( null, new StatefulContext( context, config));
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
