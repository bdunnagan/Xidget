/*
 * Stonewall Networks, Inc.
 *
 *   Project: XModelUIPlugin
 *   Author:  bdunnagan
 *   Date:    Jun 9, 2006
 *
 * Copyright 2006.  Stonewall Networks, Inc.
 */
package org.xidget.xaction;

import java.util.ArrayList;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
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
 * An action that opens a platform-specific file dialog. In single selection mode the action
 * will populate the value of the target or variable with the path name.  In multiple
 * selection mode the action will create an element to hold each path and add the nodes
 * as children to the target or variable.
 */
public class OpenFileDialogAction extends GuardedAction
{
  /* (non-Javadoc)
   * @see org.xmodel.xaction.GuardedAction#configure(org.xmodel.xaction.XActionDocument)
   */
  public void configure( XActionDocument document)
  {
    super.configure( document);
    
    variable = Xlate.get( document.getRoot(), "assign", (String)null);
    multiSelect = Xlate.get( document.getRoot(), "multi", false);
    
    windowExpr = document.getExpression( "window", true);
    descriptionExpr = document.getExpression( "description", true);
    targetExpr = document.getExpression( "target", true);
    if ( targetExpr == null) targetExpr = document.getExpression();
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected void doAction( IContext context)
  {
    IModelObject windowNode = windowExpr.queryFirst( context);
    if ( windowNode == null) throw new XActionException( "Window is undefined: "+windowExpr);
    
    IXidget xidget = (IXidget)windowNode.getValue();
    if ( xidget == null) throw new XActionException( "Window is undefined: "+windowExpr);

    String description = (descriptionExpr != null)? descriptionExpr.evaluateString( context): "";
    
    // open dialog
    IToolkit toolkit = Creator.getInstance().getToolkit();
    String[] paths = toolkit.openFileDialog( xidget, (StatefulContext)context, filterExpr, description, multiSelect);
    
    if ( multiSelect)
    {
      List<IModelObject> entries = new ArrayList<IModelObject>();
      for( String path: paths)
      {
        IModelObject entry = new ModelObject( "path");
        entry.setValue( path);
        entries.add( entry);
      }
      
      if ( targetExpr != null)
      {
        IModelObject target = targetExpr.queryFirst( context);
        if ( target != null)
        {
          target.removeChildren( "path");
          for( IModelObject entry: entries) target.addChild( entry);
        }
      }
      
      if ( variable != null)
      {
        IVariableScope scope = context.getScope();
        if ( scope != null) scope.set( variable, entries);
      }
    }
    
    // single selection mode
    else
    {
      if ( targetExpr != null)
      {
        IModelObject target = targetExpr.queryFirst( context);
        if ( target != null) target.setValue( (paths.length > 0)? paths[ 0]: "");
      }
      
      if ( variable != null)
      {
        IVariableScope scope = context.getScope();
        if ( scope != null) scope.set( variable, (paths.length > 0? paths[ 0]: ""));
      }
    }
  }

  private IExpression windowExpr;
  private IExpression targetExpr;
  private IExpression filterExpr;
  private IExpression descriptionExpr;
  private String variable;
  private boolean multiSelect;
}
