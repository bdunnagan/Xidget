/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.tree;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.feature.model.SelectionUpdateFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;

/**
 * An implementation of ISelectionUpdateFeature that recurses to the children of this xidget.
 */
public class JTreeSelectionUpdateFeature extends SelectionUpdateFeature
{
  public JTreeSelectionUpdateFeature( IXidget xidget)
  {
    super( xidget);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#updateWidget()
   */
  @Override
  public void updateWidget()
  {
    super.updateWidget();
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.updateWidget();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#updateModel()
   */
  @Override
  public void updateModel()
  {
    super.updateModel();
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.updateModel();
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#displaySelect(java.util.List)
   */
  @Override
  public void displaySelect( List<? extends Object> objects)
  {
    super.displaySelect( objects);
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.displaySelect( objects);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#displayDeselect(java.util.List)
   */
  @Override
  public void displayDeselect( List<? extends Object> objects)
  {
    super.displayDeselect( objects);
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.displayDeselect( objects);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#modelSelect(java.util.List)
   */
  @Override
  public void modelSelect( List<? extends Object> objects)
  {
    super.modelSelect( objects);
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.modelSelect( objects);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.feature.model.SelectionUpdateFeature#modelDeselect(java.util.List)
   */
  @Override
  public void modelDeselect( List<? extends Object> objects)
  {
    super.modelDeselect( objects);
    
    // recurse
    for( IXidget child: xidget.getChildren())
    {
      ISelectionUpdateFeature feature = child.getFeature( ISelectionUpdateFeature.class);
      if ( feature != null) feature.modelDeselect( objects);
    }
  }
}
