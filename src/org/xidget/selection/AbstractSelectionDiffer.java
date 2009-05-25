/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.selection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xmodel.IModelObject;
import org.xmodel.diff.AbstractListDiffer;

/**
 * A base implementation of a list differ that generates a change-set.
 */
@SuppressWarnings("unchecked")
public abstract class AbstractSelectionDiffer extends AbstractListDiffer
{
  /**
   * Returns a pointer to the specified node. The pointer can be any type of element
   * that contains enough information to identify the specified node. 
   * @param node
   * @return
   */
  public abstract IModelObject createReference( IModelObject node);
  
  /**
   * Returns a unique identity for the specified node.
   * @param node The node.
   * @return Returns a unique identity for the specified node.
   */
  public abstract Object getIdentity( IModelObject node);
  
  /* (non-Javadoc)
   * @see org.xmodel.diff.IListDiffer#notifyInsert(java.util.List, int, int, java.util.List, int, int)
   */
  public void notifyInsert( List lhs, int lIndex, int lAdjust, List rhs, int rIndex, int count)
  {
    Change change = new Change();
    change.lIndex = lIndex + lAdjust;
    change.rIndex = rIndex;
    change.count = count;
    
    if ( changes == null) changes = new ArrayList<Change>();
    changes.add( change);
  }

  /* (non-Javadoc)
   * @see org.xmodel.diff.IListDiffer#notifyRemove(java.util.List, int, int, java.util.List, int)
   */
  public void notifyRemove( List lhs, int lIndex, int lAdjust, List rhs, int count)
  {
    Change change = new Change();
    change.lIndex = lIndex + lAdjust;
    change.rIndex = -1;
    change.count = count;
    
    if ( changes == null) changes = new ArrayList<Change>();
    changes.add( change);
  }
  
  /**
   * Returns the changes.
   * @return Returns the changes.
   */
  public List<Change> getChanges()
  {
    if ( changes == null) return Collections.emptyList();
    return changes;
  }
  
  private List<Change> changes;
}
