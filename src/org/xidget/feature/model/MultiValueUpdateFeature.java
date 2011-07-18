/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.List;

import org.xidget.IXidget;
import org.xidget.ifeature.model.IMultiValueModelFeature;
import org.xidget.ifeature.model.IMultiValueUpdateFeature;
import org.xidget.ifeature.model.IMultiValueWidgetFeature;
import org.xmodel.diff.ListDiffer;
import org.xmodel.diff.ListDiffer.Change;

/**
 * Standard implementation of IMultiValueUpdateFeature.
 */
public class MultiValueUpdateFeature implements IMultiValueUpdateFeature
{
  public MultiValueUpdateFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.differ = new ListDiffer();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#updateWidget()
   */
  @Override
  public void updateWidget()
  {
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    List<? extends Object> rhs = modelFeature.getValues();
    
    IMultiValueWidgetFeature widgetFeature = xidget.getFeature( IMultiValueWidgetFeature.class);
    List<? extends Object> lhs = widgetFeature.getValues();
    
    differ.diff( lhs, rhs);
    
    List<Change> changes = differ.getChanges();
    for( Change change: changes)
    {
      if ( change.rIndex >= 0)
      {
        for( int i=0; i<change.count; i++)
        {
          displayInsert( change.lIndex + i, rhs.get( change.rIndex + i));
        }
      }
      else
      {
        for( int i=0; i<change.count; i++)
        {
          displayRemove( change.lIndex);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#updateModel()
   */
  @Override
  public void updateModel()
  {
    IMultiValueWidgetFeature widgetFeature = xidget.getFeature( IMultiValueWidgetFeature.class);
    List<? extends Object> rhs = widgetFeature.getValues();
    
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    List<? extends Object> lhs = modelFeature.getValues();
    
    differ.diff( lhs, rhs);
    
    List<Change> changes = differ.getChanges();
    for( Change change: changes)
    {
      if ( change.rIndex >= 0)
      {
        for( int i=0; i<change.count; i++)
        {
          modelInsert( change.lIndex + i, rhs.get( change.rIndex + i));
        }
      }
      else
      {
        for( int i=0; i<change.count; i++)
        {
          modelRemove( change.lIndex);
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#displayInsert(int, java.lang.Object)
   */
  @Override
  public void displayInsert( int index, Object value)
  {
    IMultiValueWidgetFeature widgetFeature = xidget.getFeature( IMultiValueWidgetFeature.class);
    widgetFeature.insertValue( index, value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#displayUpdate(int, java.lang.Object)
   */
  @Override
  public void displayUpdate( int index, Object value)
  {
    IMultiValueWidgetFeature widgetFeature = xidget.getFeature( IMultiValueWidgetFeature.class);
    widgetFeature.updateValue( index, value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#displayRemove(int)
   */
  @Override
  public void displayRemove( int index)
  {
    IMultiValueWidgetFeature widgetFeature = xidget.getFeature( IMultiValueWidgetFeature.class);
    widgetFeature.removeValue( index);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#modelInsert(int, java.lang.Object)
   */
  @Override
  public void modelInsert( int index, Object value)
  {
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    modelFeature.insertValue( index, value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#modelUpdate(int, java.lang.Object)
   */
  @Override
  public void modelUpdate( int index, Object value)
  {
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    modelFeature.updateValue( index, value);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.IMultiValueUpdateFeature#modelRemove(int)
   */
  @Override
  public void modelRemove( int index)
  {
    IMultiValueModelFeature modelFeature = xidget.getFeature( IMultiValueModelFeature.class);
    modelFeature.removeValue( index);
  }

  protected IXidget xidget;
  private ListDiffer differ;
}
