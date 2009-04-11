/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.table.column.feature;

import java.util.List;
import org.xidget.IXidget;
import org.xmodel.IModelObject;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of the IColumnEditorFeature which simply picks the first child of the column xidget.
 */
public class ColumnEditorFeature implements IColumnEditorFeature
{
  public ColumnEditorFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }
    
  /* (non-Javadoc)
   * @see org.xidget.table.column.feature.IColumnEditorFeature#getEditor()
   */
  public IXidget getEditor()
  {
    List<IXidget> children = xidget.getChildren();
    if ( children == null || children.size() == 0) return null;
    return children.get( 0);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.table.column.feature.IColumnEditorFeature#openEditor(org.xmodel.xpath.expression.StatefulContext)
   */
  public void openEditor( StatefulContext context)
  {
    // create clone
    target = context.getObject();
    clone = new StatefulContext( context, target.cloneObject());
    
    // open editor
    IXidget editor = getEditor();
    if ( editor != null)
    {
      IActivationFeature activationFeature = xidget.getFeature( IActivationFeature.class);
      activationFeature.activate( clone);
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.table.column.feature.IColumnEditorFeature#closeEditor(org.xmodel.xpath.expression.StatefulContext, boolean)
   */
  public void closeEditor( StatefulContext context, boolean commit)
  {
    if ( commit) target.setValue( clone.getObject().getValue());
    
    IXidget editor = getEditor();
    if ( editor != null)
    {
      IActivationFeature activationFeature = xidget.getFeature( IActivationFeature.class);
      activationFeature.deactivate( clone);
    }
  }

  private IXidget xidget;
  private IModelObject target;
  private StatefulContext clone;
}
