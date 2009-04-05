/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column.feature;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.feature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of IColumnBindingFeature.
 */
public class ColumnBindingFeature implements IColumnBindingFeature
{
  /**
   * Create this feature for the specified column xidget.
   * @param xidget The column xidget.
   */
  public ColumnBindingFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.features.IColumnBindingFeature#bind(int, org.xmodel.IModelObject)
   */
  public void bind( int row, IModelObject object)
  {
    // ensure capacity
    if ( contexts == null) contexts = new ArrayList<StatefulContext>();
    for( int i=contexts.size(); i<=row; i++) contexts.add( null);

    // get parent context
    IBindFeature parentBindFeature = xidget.getParent().getFeature( IBindFeature.class);
    StatefulContext parent = parentBindFeature.getBoundContexts().get( 0);
    
    // bind
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    StatefulContext context = new StatefulContext( parent, object);
    contexts.add( row, context);
    bindFeature.bind( context);
  }

  /* (non-Javadoc)
   * @see org.xidget.table.features.IColumnBindingFeature#unbind(int, org.xmodel.IModelObject)
   */
  public void unbind( int row, IModelObject object)
  {
    if ( contexts.size() <= row) return;
    
    StatefulContext context = contexts.get( row);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.unbind( context);
    
    contexts.remove( row);
  }
    
  private IXidget xidget;
  private List<StatefulContext> contexts;
}
