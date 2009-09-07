/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table;

import org.xidget.Xidget;
import org.xidget.feature.BindFeature;
import org.xidget.feature.DragAndDropFeature;
import org.xidget.feature.ScriptFeature;
import org.xidget.feature.SelectionModelFeature;
import org.xidget.feature.tree.ColumnSetFeature;
import org.xidget.feature.tree.RowSetFeature;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IDragAndDropFeature;
import org.xidget.ifeature.IScriptFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.IWidgetCreationFeature;
import org.xidget.ifeature.tree.IColumnSetFeature;
import org.xidget.ifeature.tree.IRowSetFeature;
import org.xidget.ifeature.tree.ITreeWidgetFeature;

/**
 * A xidget representing a contiguous set of rows in the table.
 */
public class SubTableXidget extends Xidget
{
  public void createFeatures()
  {
    rowSetFeature = new RowSetFeature( this);
    columnSetFeature = new ColumnSetFeature( this);
    bindFeature = new BindFeature( this, new String[] { "text", "combo", "button"});
    selectionModelFeature = new SelectionModelFeature( this);
    scriptFeature = new ScriptFeature( this);
    dndFeature = new DragAndDropFeature( this);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.Xidget#getFeature(java.lang.Class)
   */
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    if ( clss == IColumnSetFeature.class) return (T)columnSetFeature;
    if ( clss == IRowSetFeature.class) return (T)rowSetFeature;
    if ( clss == IBindFeature.class) return (T)bindFeature;
    if ( clss == ISelectionModelFeature.class) return (T)selectionModelFeature;
    if ( clss == ITreeWidgetFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IAsyncFeature.class) return (T)getParent().getFeature( clss);
    if ( clss == IWidgetCreationFeature.class) return (T)getParent().getFeature( clss);
    
    if ( clss == IScriptFeature.class) return (T)scriptFeature;
    if ( clss == IDragAndDropFeature.class) return (T)dndFeature;
    
    return super.getFeature( clss);
  }
  
  private IRowSetFeature rowSetFeature;
  private IColumnSetFeature columnSetFeature;
  private IBindFeature bindFeature;
  private ISelectionModelFeature selectionModelFeature;
  private IScriptFeature scriptFeature;
  private IDragAndDropFeature dndFeature;
}
