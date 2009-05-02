/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tree;

import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.table.TreeTableWidgetFeature;
import org.xidget.feature.tree.TreeExpandFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IWidgetFeature;
import org.xidget.ifeature.table.ITableWidgetFeature;
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
    tableWidgetFeature = new TreeTableWidgetFeature( this);
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
    if ( clss == ITableWidgetFeature.class) return (T)tableWidgetFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IWidgetFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IErrorFeature.class) return (T)getParent().getFeature( clss);
    
    return super.getFeature( clss);
  }

  private IBindFeature bindFeature;
  private ITreeExpandFeature expandFeature;
  private TreeTableWidgetFeature tableWidgetFeature;
}
