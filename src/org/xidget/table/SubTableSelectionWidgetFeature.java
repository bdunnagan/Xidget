/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.model.IPartialSelectionWidgetFeature;
import org.xidget.ifeature.model.ISelectionWidgetFeature;

/**
 * An implementation of ISelectionWidgetFeature that manages the portion of the widget selection associated
 * with a nested xidget such as a sub-table or sub-tree declaration.
 */
public class SubTableSelectionWidgetFeature implements ISelectionWidgetFeature
{
  public SubTableSelectionWidgetFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    IPartialSelectionWidgetFeature feature = xidget.getParent().getFeature( IPartialSelectionWidgetFeature.class);
    feature.select( xidget, object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    IPartialSelectionWidgetFeature feature = xidget.getParent().getFeature( IPartialSelectionWidgetFeature.class);
    feature.deselect( xidget, object);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> list)
  {
    IPartialSelectionWidgetFeature feature = xidget.getParent().getFeature( IPartialSelectionWidgetFeature.class);
    feature.setSelection( xidget, list);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionWidgetFeature#getSelection()
   */
  @Override
  public List<? extends Object> getSelection()
  {
    IPartialSelectionWidgetFeature feature = xidget.getParent().getFeature( IPartialSelectionWidgetFeature.class);
    return feature.getSelection( xidget);
  }

  private IXidget xidget;
}
