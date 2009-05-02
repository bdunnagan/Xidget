/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.tree;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.feature.table.RowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;
import org.xidget.table.Row;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A final implementation of RowSetFeature which stores its rows in the parent row associated with the input context.
 */
public class TreeRowSetFeature extends RowSetFeature
{
  public TreeRowSetFeature( IXidget xidget)
  {
    super( xidget);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IRowSetFeature#getRows(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<IModelObject> getRows( StatefulContext context)
  {
    ITreeWidgetFeature feature = xidget.getFeature( ITreeWidgetFeature.class);
    List<IModelObject> children = new ArrayList<IModelObject>();
    Row parent = feature.findRow( context);
    if ( parent != null)
    {
      for( Row child: parent.getChildren())
        children.add( child.getContext().getObject());
    }
    return children;
  }
}
