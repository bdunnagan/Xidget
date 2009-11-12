/*
 * Xidget - XML Widgets based on JAHM
 * 
 * ReferenceListDiffer.java
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
   * @see org.xidget.selection.AbstractSelectionDiffer#getIdentity(org.xmodel.IModelObject)
   */
  @Override
  public Object getIdentity( IModelObject node)
  {
    return node.getReferent();
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
