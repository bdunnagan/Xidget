/*
 * Xidget - XML Widgets based on JAHM
 * 
 * SelectionModelFeature.java
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
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xidget.ifeature.ISelectionWidgetFeature;
import org.xidget.selection.AbstractSelectionDiffer;
import org.xidget.selection.Change;
import org.xidget.selection.ForeignKeyListDiffer;
import org.xidget.selection.ReferenceListDiffer;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * A default implementation of ISelectionModelFeature that diffs new nodes into the selection parent.
 */
public class SelectionModelFeature implements ISelectionModelFeature
{
  public SelectionModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#configure(org.xmodel.IModelObject)
   */
  public void configure( IModelObject element)
  {
    filterExpr = Xlate.childGet( element, "filter", Xlate.get( element, "filter", (IExpression)null));
    
    // choose the differ
    String srcAttr = Xlate.childGet( element, "srcAttr", Xlate.get( element, "srcAttr", (String)null));
    String dstAttr = Xlate.childGet( element, "dstAttr", Xlate.get( element, "dstAttr", (String)null));
    if ( srcAttr == null && dstAttr == null)
    {
      differ = new ReferenceListDiffer( Xlate.get( element, "dereference", "one").equals( "all"));
    }
    else
    {
      differ = new ForeignKeyListDiffer( srcAttr, dstAttr);
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setParent(org.xmodel.xpath.expression.StatefulContext, org.xmodel.IModelObject)
   */
  public void setParent( StatefulContext context, IModelObject element)
  {
    SelectionListener listener = new SelectionListener( this, context);
    if ( parent != null) parent.removeModelListener( listener);
    if ( element != null) element.addModelListener( listener);
    
    parent = element;
    
    try
    {
      updating = true;
      
      // update widget selection
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      if ( feature != null) 
      {
        if ( parent != null) feature.setSelection( applyFilter( context, parent.getChildren()));
        else feature.setSelection( Collections.<IModelObject>emptyList());
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#insertSelected(org.xmodel.xpath.expression.StatefulContext, int, org.xmodel.IModelObject)
   */
  public void insertSelected( StatefulContext context, int index, IModelObject element)
  {
    if ( parent == null || updating) return;
    updating = true;
    
    parent.addChild( element, index);
    
    updating = false;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#removeSelected(org.xmodel.xpath.expression.StatefulContext, int, org.xmodel.IModelObject)
   */
  public void removeSelected( StatefulContext context, int index, IModelObject element)
  {
    if ( parent == null || updating) return;
    updating = true;
    
    parent.removeChild( index);
    
    updating = false;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setSelection(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  public void setSelection( StatefulContext context, List<IModelObject> nodes)
  {
    if ( parent == null || updating) return;
    updating = true;

    try
    {
      // filter
      nodes = applyFilter( context, nodes);
      
      // diff
      differ.diff( parent.getChildren(), nodes);
      List<Change> changes = differ.getChanges();
      for( Change change: changes)
      {
        // add
        if ( change.rIndex >= 0)
          for( int i=0; i<change.count; i++)
            parent.addChild( differ.createReference( nodes.get( change.rIndex + i)), change.lIndex + i);
        
        // remove
        else
        {
          for( int i=0; i<change.count; i++)
            parent.removeChild( change.lIndex);
        }
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#getSelection(org.xmodel.xpath.expression.StatefulContext)
   */
  public List<IModelObject> getSelection( StatefulContext context)
  {
    if ( parent == null) return Collections.emptyList();
    return applyFilter( context, parent.getChildren());
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#getIdentity(org.xmodel.IModelObject)
   */
  public Object getIdentity( IModelObject node)
  {
    return differ.getIdentity( node);
  }

  /**
   * Returns a new pointer to the specified node. The pointer can be any type of element
   * which contains enough information to identify the specified node.
   * @param node The node to be referenced.
   * @return Returns a new pointer to the specified node.
   */
  protected IModelObject createReference( IModelObject node)
  {
    return differ.createReference( node);
  }
  
  /**
   * Returns the filtered set of nodes.
   * @param context The parent context of filter evaluation.
   * @param nodes The nodes to be filtered.
   * @return Returns the filtered set of nodes.
   */
  protected List<IModelObject> applyFilter( StatefulContext context, List<IModelObject> nodes)
  {
    if ( filterExpr == null) return nodes;

    List<IModelObject> filtered = new ArrayList<IModelObject>( nodes.size());
    for( IModelObject node: nodes)
      if ( filterExpr.evaluateBoolean( new StatefulContext( context, node)))
        filtered.add( node);
    
    return filtered;
  }
  
  /**
   * Returns the index of the specified child in the filtered list.
   * @param context The context.
   * @param children The complete list of children of the selection parent.
   * @param child The child.
   * @return Returns -1 or the index of the specified child in the filtered list.
   */
  private int findFilterIndex( StatefulContext context, List<IModelObject> children, IModelObject child)
  {
    List<IModelObject> elements = applyFilter( context, children);
    return elements.indexOf( child);
  }

  /**
   * A listener for the selection elements.
   */
  private static class SelectionListener extends ModelListener
  {
    public SelectionListener( SelectionModelFeature selectionModelFeature, StatefulContext context)
    {
      this.selectionModelFeature = selectionModelFeature;
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyAddChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      if ( selectionModelFeature.updating) return;
      selectionModelFeature.updating = true;
      
      try
      {
        ISelectionWidgetFeature feature = selectionModelFeature.xidget.getFeature( ISelectionWidgetFeature.class);
        if ( feature == null) return;
          
        int filterIndex = selectionModelFeature.findFilterIndex( context, parent.getChildren(), child);
        if ( filterIndex >= 0) feature.insertSelected( filterIndex, child);
      }
      finally
      {
        selectionModelFeature.updating = false;
      }
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyRemoveChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      if ( selectionModelFeature.updating) return;
      selectionModelFeature.updating = true;
      
      try
      {
        ISelectionWidgetFeature feature = selectionModelFeature.xidget.getFeature( ISelectionWidgetFeature.class);
        if ( feature == null) return;
        
        List<IModelObject> children = new ArrayList<IModelObject>( parent.getChildren());
        children.add( index, child);
        
        int filterIndex = selectionModelFeature.findFilterIndex( context, children, child);
        if ( filterIndex >= 0) feature.removeSelected( filterIndex, child);
      }
      finally
      {
        selectionModelFeature.updating = false;
      }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( !(object instanceof SelectionListener)) return false;
      SelectionListener listener = (SelectionListener)object;
      return listener.context.equals( context) && listener.selectionModelFeature == selectionModelFeature;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      // don't get this confused with other listeners hashed by context
      return context.hashCode() + getClass().hashCode() + selectionModelFeature.hashCode();
    }

    private SelectionModelFeature selectionModelFeature;
    private StatefulContext context;
  };
  
  protected IXidget xidget;
  private AbstractSelectionDiffer differ;
  private IExpression filterExpr;
  private IModelObject parent;
  private boolean updating;
}
