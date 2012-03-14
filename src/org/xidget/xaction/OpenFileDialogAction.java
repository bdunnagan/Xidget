/*
 * Xidget - XML Widgets based on JAHM
 * 
 * OpenFileDialogAction.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.xidget.Creator;
import org.xidget.IToolkit;
import org.xidget.IToolkit.FileDialogType;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.xaction.Conventions;
import org.xmodel.xaction.GuardedAction;
import org.xmodel.xaction.XActionDocument;
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
    
    var = Conventions.getVarName( document.getRoot(), false, "assign");
    
    typeExpr = document.getExpression( "type", true);
    if ( typeExpr == null) typeExpr = XPath.createExpression( "'openOne'");
    
    folderExpr = document.getExpression( "folder", true);
    filterExpr = document.getExpression( "filter", true);
    dfaultExpr = document.getExpression( "default", true);
    descExpr = document.getExpression( "description", true);
    targetExpr = document.getExpression( "target", true);
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.swt.form.actions.GuardedAction#doAction(org.xmodel.xpath.expression.IContext)
   */
  protected Object[] doAction( IContext context)
  {
    FileDialogType type = FileDialogType.valueOf( typeExpr.evaluateString( context));
    String desc = (descExpr != null)? descExpr.evaluateString( context): "";

    // initialize the variable
    if ( var != null)
    {
      IVariableScope scope = context.getScope();
      if ( scope != null) scope.set( var, Collections.<IModelObject>emptyList());
    }
    
    String dfault = (dfaultExpr != null)? dfaultExpr.evaluateString( context): null;
    
    // open dialog
    IToolkit toolkit = Creator.getToolkit();
    String[] paths = toolkit.openFileDialog( (StatefulContext)context, folderExpr, filterExpr, dfault, desc, type);
    
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
      
      if ( var != null)
      {
        IVariableScope scope = context.getScope();
        if ( scope != null) scope.set( var, entries);
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
      
      if ( var != null)
      {
        IVariableScope scope = context.getScope();
        if ( scope != null) scope.set( var, (paths.length > 0? paths[ 0]: ""));
      }
    }
    
    return null;
  }

  private IExpression targetExpr;
  private IExpression folderExpr;
  private IExpression filterExpr;
  private IExpression dfaultExpr;
  private IExpression descExpr;
  private IExpression typeExpr;
  private String var;
}
