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
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;
import org.xmodel.xpath.variable.IVariableListener;
import org.xmodel.xpath.variable.IVariableScope;

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
    ParentSelectionListener listener = new ParentSelectionListener( this, context);
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
        if ( parent != null) 
        {
          feature.setSelection( applyFilter( context, parent.getChildren()));
        }
        else 
        {
          feature.setSelection( Collections.<IModelObject>emptyList());
        }
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setVariable(org.xmodel.xpath.expression.StatefulContext, java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public void setVariable( StatefulContext context, String newVariable)
  {
    VariableSelectionListener listener = new VariableSelectionListener( this, context);
    if ( variable != null) context.getScope().removeListener( variable, context, listener);
    if ( newVariable != null) 
    {
      if ( !context.getScope().isBound( newVariable)) context.set( newVariable, Collections.<IModelObject>emptyList());
      context.getScope().addListener( newVariable, context, listener);
    }
    
    variable = newVariable;
    
    try
    {
      updating = true;
      
      // update widget selection
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      if ( feature != null) 
      {
        if ( variable != null) 
        {
          Object value = context.get( variable);
          if ( value instanceof List)
          {
            feature.setSelection( applyFilter( context, (List<? extends Object>)value));
          }
          else
          {
            feature.setSelection( applyFilter( context, Collections.singletonList( value)));
          }
        }
        else 
        {
          feature.setSelection( Collections.emptyList());
        }
      }
    }
    finally
    {
      updating = false;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#insertSelected(org.xmodel.xpath.expression.StatefulContext, int, java.lang.Object)
   */
  public void insertSelected( StatefulContext context, int index, Object object)
  {
    if ( parent == null || updating) return;
    updating = true;
    
    if ( parent != null && object instanceof IModelObject) 
    {
      parent.addChild( (IModelObject)object, index);
    }
    
    if ( variable != null)
    {
      if ( object instanceof List) context.getScope().insert( variable, object, index);
      else if ( object instanceof Number) context.set( variable, (Number)object);
      else if ( object instanceof Boolean) context.set( variable, (Boolean)object);
      else context.set( variable, object.toString());
    }
    
    updating = false;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#removeSelected(org.xmodel.xpath.expression.StatefulContext, int, java.lang.Object)
   */
  public void removeSelected( StatefulContext context, int index, Object object)
  {
    if ( parent == null || updating) return;
    updating = true;
    
    if ( parent != null && object instanceof IModelObject) 
    {
      parent.removeChild( index);
    }
    
    if ( variable != null)
    {
      if ( object instanceof List) context.getScope().remove( variable, index);
      else if ( object instanceof Number) context.set( variable, (Number)object);
      else if ( object instanceof Boolean) context.set( variable, (Boolean)object);
      else context.set( variable, object.toString());
    }
    
    updating = false;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#setSelection(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  @SuppressWarnings("unchecked")
  public void setSelection( StatefulContext context, List<? extends Object> objects)
  {
    if ( updating) return;
    updating = true;

    try
    {
      // filter
      objects = applyFilter( context, objects);
      List<? extends Object> children = applyFilter( context, (parent != null)? parent.getChildren(): (List<Object>)context.get( variable));
      
      // diff
      differ.diff( children, objects);
      List<Change> changes = differ.getChanges();
      for( Change change: changes)
      {
        // add
        if ( change.rIndex >= 0)
        {
          for( int i=0; i<change.count; i++)
          {
            Object object = objects.get( change.rIndex + i);
            if ( parent != null && object instanceof IModelObject) 
              parent.addChild( differ.createReference( (IModelObject)object), change.lIndex + i);
            if ( variable != null)
              context.getScope().insert( variable, object, change.lIndex + i);
          }
        }
        
        // remove
        else
        {
          for( int i=0; i<change.count; i++)
          {
            Object object = children.get( change.lIndex);
            if ( parent != null && object instanceof IModelObject)
              parent.removeChild( (IModelObject)object);
            if ( variable != null)
              context.getScope().remove( variable, object);
          }
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
  @SuppressWarnings("unchecked")
  public List<? extends Object> getSelection( StatefulContext context)
  {
    if ( parent != null) return applyFilter( context, parent.getChildren());
    if ( variable != null) return applyFilter( context, (List<Object>)context.get( variable));
    return Collections.emptyList();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.ISelectionModelFeature#getIdentity(java.lang.Object)
   */
  public Object getIdentity( Object object)
  {
    if ( object instanceof IModelObject) return differ.getIdentity( (IModelObject)object);
    return object;
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
  protected List<? extends Object> applyFilter( StatefulContext context, List<? extends Object> nodes)
  {
    if ( filterExpr == null) return nodes;

    List<Object> filtered = new ArrayList<Object>( nodes.size());
    for( Object node: nodes)
    {
      // Assume node is IModelObject until XPath 2.0 sequences are supported.
      if ( filterExpr.evaluateBoolean( new StatefulContext( context, (IModelObject)node)))
        filtered.add( node);
    }
    
    return filtered;
  }
  
  /**
   * Returns the index of the specified child in the filtered list.
   * @param context The context.
   * @param children The complete list of children of the selection parent.
   * @param child The child.
   * @return Returns -1 or the index of the specified child in the filtered list.
   */
  private int findFilterIndex( StatefulContext context, List<? extends Object> children, Object child)
  {
    List<? extends Object> elements = applyFilter( context, children);
    return elements.indexOf( child);
  }

  /**
   * Insert the specified object into the selection.
   * @param context The context of the selection update.
   * @param object The object.
   */
  @SuppressWarnings("unchecked")
  private void insertSelected( StatefulContext context, Object object)
  {
    if ( updating) return;
    updating = true;
    try
    {
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      if ( feature != null)
      {
        List<? extends Object> list = (parent != null)? parent.getChildren(): (List<? extends Object>)context.get( variable);
        int filterIndex = findFilterIndex( context, list, object);
        if ( filterIndex >= 0) feature.insertSelected( filterIndex, object);
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /**
   * Remove the specified object from the selection.
   * @param context The context of the selection update.
   * @param object The object.
   */
  @SuppressWarnings("unchecked")
  private void removeSelected( StatefulContext context, Object object)
  {
    if ( updating) return;
    updating = true;
    try
    {
      ISelectionWidgetFeature feature = xidget.getFeature( ISelectionWidgetFeature.class);
      if ( feature != null)
      {
        List<? extends Object> list = (parent != null)? parent.getChildren(): (List<? extends Object>)context.get( variable);
        int filterIndex = findFilterIndex( context, list, object);
        if ( filterIndex >= 0) feature.removeSelected( filterIndex, object);
      }
    }
    finally
    {
      updating = false;
    }
  }
  
  /**
   * An IModelListener for the selection elements.
   */
  private static class ParentSelectionListener extends ModelListener
  {
    public ParentSelectionListener( SelectionModelFeature owner, StatefulContext context)
    {
      this.owner = owner;
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyAddChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      owner.insertSelected( context, child);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyRemoveChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      owner.removeSelected( context, child);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( !(object instanceof ParentSelectionListener)) return false;
      ParentSelectionListener listener = (ParentSelectionListener)object;
      return listener.context.equals( context) && listener.owner == owner;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      // don't get this confused with other listeners hashed by context
      return context.hashCode() + getClass().hashCode() + owner.hashCode();
    }

    private SelectionModelFeature owner;
    private StatefulContext context;
  };
  
  /**
   * An IVariableListener for the selection elements.
   */
  private static class VariableSelectionListener implements IVariableListener
  {
    public VariableSelectionListener( SelectionModelFeature owner, StatefulContext context)
    {
      this.owner = owner;
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyAdd(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      for( IModelObject node: nodes) owner.insertSelected( this.context, node);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyRemove(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( String name, IVariableScope scope, IContext context, List<IModelObject> nodes)
    {
      for( IModelObject node: nodes) owner.removeSelected( this.context, node);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, String newValue, String oldValue)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Number, java.lang.Number)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Number newValue, Number oldValue)
    {
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.variable.IVariableListener#notifyChange(java.lang.String, org.xmodel.xpath.variable.IVariableScope, org.xmodel.xpath.expression.IContext, java.lang.Boolean)
     */
    @Override
    public void notifyChange( String name, IVariableScope scope, IContext context, Boolean newValue)
    {
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( !(object instanceof VariableSelectionListener)) return false;
      VariableSelectionListener listener = (VariableSelectionListener)object;
      return listener.context.equals( context) && listener.owner == owner;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      // don't get this confused with other listeners hashed by context
      return context.hashCode() + getClass().hashCode() + owner.hashCode();
    }

    private SelectionModelFeature owner;
    private StatefulContext context;
  };
  
  protected IXidget xidget;
  private AbstractSelectionDiffer differ;
  private IExpression filterExpr;
  private IModelObject parent;
  private String variable;
  private boolean updating;
}
