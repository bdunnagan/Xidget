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
import java.util.Collections;
import java.util.List;
import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IXidget;
import org.xidget.IToolkit.FileDialogType;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Xlate;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
import org.xmodel.xaction.XActionException;
import org.xmodel.xpath.XPath;
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
    
    typeExpr = document.getExpression( "type", true);
    if ( typeExpr == null) typeExpr = XPath.createExpression( "'openOne'");
    
    windowExpr = document.getExpression( "window", true);
    filterExpr = document.getExpression( "filter", true);
    descriptionExpr = document.getExpression( "description", true);
    targetExpr = document.getExpression( "target", true);
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected Object[] doAction( IContext context)
  {
    IModelObject windowNode = windowExpr.queryFirst( context);
    if ( windowNode == null) throw new XActionException( "Window is undefined: "+windowExpr);
    
    IXidget xidget = (IXidget)windowNode.getValue();
    if ( xidget == null) throw new XActionException( "Window is undefined: "+windowExpr);

    FileDialogType type = FileDialogType.valueOf( typeExpr.evaluateString( context));
    String description = (descriptionExpr != null)? descriptionExpr.evaluateString( context): "";

    // initialize the variable
    if ( variable != null)
    {
      IVariableScope scope = context.getScope();
      if ( scope != null) scope.set( variable, Collections.<IModelObject>emptyList());
    }
    
    // open dialog
    IToolkit toolkit = Creator.getInstance().getToolkit();
    String[] paths = toolkit.openFileDialog( xidget, (StatefulContext)context, filterExpr, description, type);
    
    if ( type == FileDialogType.openMany)
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
    
    return null;
  }

  private IExpression windowExpr;
  private IExpression targetExpr;
  private IExpression filterExpr;
  private IExpression descriptionExpr;
  private IExpression typeExpr;
  private String variable;
}
