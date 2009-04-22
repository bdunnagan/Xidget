/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.table;

import java.util.ArrayList;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.table.IGroupOffsetFeature;
import org.xidget.ifeature.table.IRowSetFeature;

/**
 * A default implementation of IGroupOffsetFeature.
 */
public class GroupOffsetFeature implements IGroupOffsetFeature
{
  public GroupOffsetFeature( IXidget xidget)
  {
    preceding = new ArrayList<IRowSetFeature>( 5);
    
    String type = xidget.getConfig().getType();
    for( IXidget child: xidget.getParent().getChildren())
    {
      if ( child.getConfig() == xidget.getConfig()) break;
      if ( child.getConfig().isType( type))
      {
        IRowSetFeature feature = child.getFeature( IRowSetFeature.class);
        preceding.add( feature);
      }
    }
  }
    
  /* (non-Javadoc)
   * @see org.xidget.ifeature.table.IGroupOffsetFeature#getOffset()
   */
  public int getOffset()
  {
    int offset = 0;
    for( IRowSetFeature feature: preceding)
      offset += feature.getRowCount();
    return offset;
  }
  
  private List<IRowSetFeature> preceding;
}
