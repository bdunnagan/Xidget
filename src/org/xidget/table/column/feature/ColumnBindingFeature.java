/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.binding.IconBindingRule;
import org.xidget.text.binding.TextBindingRule;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IColumnBindingFeature.
 */
public class ColumnBindingFeature implements IColumnBindingFeature
{
  private final static String iconElementName = "image";
  private final static String textElementName = "source";
  
  /**
   * Create this feature for the specified column xidget.
   * @param xidget The column xidget.
   */
  public ColumnBindingFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.iconExpr = Xlate.childGet( xidget.getConfig(), iconElementName, (IExpression)null);
    this.nodeExpr = Xlate.childGet( xidget.getConfig(), textElementName, (IExpression)null);
    this.textBindingRule = new TextBindingRule();
    this.iconBindingRule = new IconBindingRule();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.IColumnBindingFeature#bind(int, org.xmodel.IModelObject)
   */
  public void bind( int row, IModelObject object)
  {
    grow( row);

    // create row data
    RowDatum rowDatum = new RowDatum();
    rowData.add( row, rowDatum);
    rowDatum.context = new StatefulContext( xidget.getContext(), object);
    
    // bind column text
    rowDatum.textListener = textBindingRule.getListener( xidget, xidget.getConfig().getFirstChild( textElementName));
    nodeExpr.addNotifyListener( rowDatum.context, rowDatum.textListener);

    // bind column icon
    rowDatum.iconListener = iconBindingRule.getListener( xidget, xidget.getConfig().getFirstChild( iconElementName));
    iconExpr.addNotifyListener( rowDatum.context, rowDatum.iconListener);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.IColumnBindingFeature#unbind(int, org.xmodel.IModelObject)
   */
  public void unbind( int row, IModelObject object)
  {
    RowDatum rowDatum = rowData.remove( row);

    // unbind column text and icon
    nodeExpr.removeListener( rowDatum.context, rowDatum.textListener);
    iconExpr.removeListener( rowDatum.context, rowDatum.iconListener);
  }
  
  /**
   * Grow the row information list to accomodate the row with the specified index.
   * @param row The row index.
   */
  private void grow( int row)
  {
    if ( rowData == null) rowData = new ArrayList<RowDatum>();
    for( int i=rowData.size(); i<=row; i++) rowData.add( null);
  }
  
  private class RowDatum
  {
    public StatefulContext context;
    public IExpressionListener textListener;
    public IExpressionListener iconListener;
  }
  
  private IXidget xidget;
  private IExpression iconExpr;
  private IExpression nodeExpr;
  private TextBindingRule textBindingRule;
  private IconBindingRule iconBindingRule;
  private List<RowDatum> rowData;
}
