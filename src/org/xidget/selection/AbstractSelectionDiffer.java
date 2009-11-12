/*
 * Xidget - XML Widgets based on JAHM
 * 
 * AbstractSelectionDiffer.java
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
   * @see org.xmodel.diff.AbstractListDiffer#diff(java.util.List, java.util.List)
   */
  @Override
  public void diff( List lhs, List rhs)
  {
    changes = null;
    super.diff( lhs, rhs);
  }

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
