/*
 * Xidget - XML Widgets based on JAHM
 * 
 * SubTreeXidget.java
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
package org.xidget.tree;

import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.tree.TreeExpandFeature;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.tree.ITreeExpandFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;

/**
 * A xidget representing the children of a tree xidget. Like the tree xidget,
 * the children are represented by one or more sub-tables.
 */
public class SubTreeXidget extends Xidget
{
  /* (non-Javadoc)
   * @see org.xidget.Xidget#createFeatures()
   */
  @Override
  protected void createFeatures()
  {
    bindFeature = new BindFeature( this, new String[] { "tree"});
    expandFeature = new TreeExpandFeature( this);
  }

  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == ITreeExpandFeature.class) return (T)expandFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IWidgetFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IAsyncFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IWidgetCreationFeature.class) return (T)getParent().getFeature( clss);
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private ITreeExpandFeature expandFeature;
}
