/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ColumnImageListener.java
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
package org.xidget.feature.tree;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.tree.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ExpressionListener which updates the image of a table cell.
 */
public class ColumnImageListener extends ExpressionListener
{
  public ColumnImageListener( IXidget xidget, Row row, int columnIndex)
  {
    this.xidget = xidget;
    this.row = row;
    this.columnIndex = columnIndex;
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes = expression.query( context, null);
    
    Object value = nodes.get( 0).getValue();
    row.getCell( columnIndex).image = value;
        
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext, java.util.List)
   */
  @Override
  public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
  {
    nodes = expression.query( context, null);
    
    Object value = (nodes.size() > 0)? nodes.get( 0).getValue(): null;
    row.getCell( columnIndex).image = value;
    
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
   * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
   */
  @Override
  public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
  {
    row.getCell( columnIndex).image = newValue;
    
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    feature.updateCell( row, columnIndex);
  }
  
  /* (non-Javadoc)
   * @see org.xmodel.xpath.expression.ExpressionListener#requiresValueNotification()
   */
  @Override
  public boolean requiresValueNotification()
  {
    return true;
  }

  private IXidget xidget;
  private Row row;
  private int columnIndex;
}
