/*
 * Xidget - XML Widgets based on JAHM
 * 
 * DragAndDropFeature.java
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
package org.xidget.feature;

import org.xidget.IXidget;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IDragAndDropFeature that allows subclasses to populate variables
 * on the context in which the <i>onDrag</i> and <i>onDrop</i> scripts are executed.
 */
public class DragAndDropFeature implements IDragAndDropFeature
{
  public DragAndDropFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#isDragEnabled()
   */
  public boolean isDragEnabled()
  {
    return xidget.getConfig().getFirstChild( "onDrag") != null; 
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#isDropEnabled()
   */
  public boolean isDropEnabled()
  {
    return xidget.getConfig().getFirstChild( "onDrop") != null; 
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#canDrag(org.xmodel.xpath.expression.StatefulContext)
   */
  public boolean canDrag( StatefulContext context)
  {
    if ( dragExpr == null)
    {
      String dragSpec = Xlate.get( xidget.getConfig().getFirstChild( "onDrag"), "when", "true()");
      dragExpr = XPath.createExpression( dragSpec);
    }
    
    return dragExpr.evaluateBoolean( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#canDrop(org.xmodel.xpath.expression.StatefulContext)
   */
  public boolean canDrop( StatefulContext context)
  {
    if ( dropExpr == null)
    {
      String dropSpec = Xlate.get( xidget.getConfig().getFirstChild( "onDrop"), "when", "true()");
      dropExpr = XPath.createExpression( dropSpec);
    }
    
    return dropExpr.evaluateBoolean( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#drag(org.xmodel.xpath.expression.StatefulContext)
   */
  public void drag( StatefulContext context)
  {
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    scriptFeature.runScript( "onDrag", context);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDragAndDropFeature#drop(org.xmodel.xpath.expression.StatefulContext)
   */
  public void drop( StatefulContext context)
  {
    IScriptFeature scriptFeature = xidget.getFeature( IScriptFeature.class);
    scriptFeature.runScript( "onDrop", context);
  }
  
  protected IXidget xidget;
  private IExpression dragExpr;
  private IExpression dropExpr;
}
