/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.selection;

import org.xmodel.IModelObject;
import org.xmodel.Reference;

/**
 * A shallow list differ for use with instances of Reference.
 */
public class ReferenceListDiffer extends AbstractSelectionDiffer
{
  /**
   * Create a differ that operates in one of the following ways: if the deep flag is true
   * then references will be fully deferenced for comparison, otherwise references will 
   * only be dereferenced one level.
   * @param deep True if references should be completely dereferenced.
   */
  public ReferenceListDiffer( boolean deep)
  {
    this.deep = deep;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.selection.AbstractSelectionDiffer#createReference(org.xmodel.IModelObject)
   */
  @Override
  public IModelObject createReference( IModelObject node)
  {
    return new Reference( node);
  }

  /* (non-Javadoc)
   * @see org.xmodel.diff.AbstractListDiffer#isMatch(java.lang.Object, java.lang.Object)
   */
  @Override
  public boolean isMatch( Object object1, Object object2)
  {
    if ( deep) return super.isMatch( object1, object2);
    
    IModelObject node1 = (IModelObject)object1;
    IModelObject node2 = (IModelObject)object2;
    
    return node1.getReferent().equals( node2.getReferent());
  }
  
  private boolean deep;
}
