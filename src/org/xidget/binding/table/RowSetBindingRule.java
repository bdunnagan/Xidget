/*
 * Xidget - XML Widgets based on JAHM
 * 
 * RowSetBindingRule.java
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
package org.xidget.binding.table;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IBindingRule;
import org.xidget.config.TagProcessor;
import org.xidget.ifeature.tree.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A binding rule for the row-set of a table.
 */
public class RowSetBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return element.getParent().isType( "table") && xidget.getFeature( ITreeWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element)
  {
    return new Listener( xidget);
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IRowSetFeature feature = xidget.getFeature( IRowSetFeature.class);
      feature.setRows( (StatefulContext)context, nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IRowSetFeature feature = xidget.getFeature( IRowSetFeature.class);
      feature.setRows( (StatefulContext)context, nodes);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    private IXidget xidget;
  }
}
