/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ColumnTitleBindingRule.java
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
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;

/**
 * A binding rule for the column title of a table.
 */
public class ColumnTitleBindingRule implements IBindingRule
{
  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#applies(org.xidget.IXidget, org.xmodel.IModelObject)
   */
  public boolean applies( IXidget xidget, IModelObject element)
  {
    return (element.getParent().isType( "column") || element.getParent().isType( "cell")) &&
      xidget.getFeature( ITreeWidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.IBindingRule#getListener(org.xidget.IXidget)
   */
  public IExpressionListener getListener( TagProcessor processor, IXidget xidget, IModelObject element)
  {
    return new Listener( xidget, element);
  }

  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget, IModelObject element)
    {
      this.xidget = xidget;
      this.element = element;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, boolean)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, boolean newValue)
    {
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, Boolean.toString( newValue));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, Double.toString( newValue));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, newValue);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, Xlate.get( node, ""));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, Xlate.get( node, ""));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      int column = getColumnIndex();
      ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
      feature.setTitle( column, (newValue != null)? newValue.toString(): "");
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
     */
    @Override
    public boolean requiresValueNotification()
    {
      return true;
    }

    /**
     * @return Returns the column index of the specified column parameter.
     */
    private int getColumnIndex()
    {
      IModelObject parent = element.getParent();
      if ( parent.isType( "column") || parent.isType( "cell"))
      {
        element = parent;
        parent = parent.getParent();
        
      }
      List<IModelObject> columns = parent.getChildren( element.getType());
      return columns.indexOf( element);
    }
    
    private IXidget xidget;
    private IModelObject element;
  }
}
