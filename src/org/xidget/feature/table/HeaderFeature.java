/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.IHeaderFeature;
import org.xidget.table.Header;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;

/**
 * A default implementation of IHeaderFeature.
 */
public class HeaderFeature implements IHeaderFeature
{
  public HeaderFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.headers = new ArrayList<Header>( 5);
    configure( xidget.getConfig());
  }
  
  /**
   * Configure the columns of the table.
   * @param element The table configuration element.
   */
  private void configure( IModelObject element)
  {
    int index = 0;
    for( IModelObject columnElement: element.getChildren( "column"))
    {
      Header header = new Header();
      header.titleExpr = getTitleExpr( columnElement);
      header.titleListener = new ColumnTitleListener( xidget, index++);  
      headers.add( header);
    }
  }
  
  /**
   * Returns the title expression from the element either from the value or from the child <i>title</i>.
   * @param element The header element.
   * @return Returns the title expression.
   */
  private IExpression getTitleExpr( IModelObject element)
  {
    IExpression titleExpr = Xlate.childGet( element, "title", (IExpression)null);
    if ( titleExpr == null) titleExpr = Xlate.get( element, (IExpression)null);
    return titleExpr;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IHeaderFeature#getHeaders()
   */
  public List<Header> getHeaders()
  {
    return headers;
  }

  private IXidget xidget;
  private List<Header> headers;
}
