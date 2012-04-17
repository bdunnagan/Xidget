/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.model.ISelectionModelFeature;
import org.xidget.ifeature.model.ISelectionUpdateFeature;
import org.xmodel.IModelListener;
import org.xmodel.IModelObject;
import org.xmodel.ModelListener;
import org.xmodel.Reference;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * The standard implementation of ISelectionModelFeature responsible for storing and retrieving the selection
 * from the assigned variable in the current context.
 */
public class SelectionModelFeature implements ISelectionModelFeature
{
  public SelectionModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.mode = Mode.ref;
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
    StatefulContext context = getContext();
    
    // remove old listener with notification
    if ( varName != null) varExpr.removeNotifyListener( context, variableListener);
    
    // set variable and expression
    varName = name;
    varExpr = XPath.createExpression( "$"+varName, false);
    
    // make sure variable is defined in this scope
    if ( !context.getScope().isDefined( varName))
    {
      context.getScope().set( varName, Collections.emptyList());
    }
    
    // add new listener
    if ( varName != null) varExpr.addNotifyListener( context, variableListener);
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setSourceNode(org.xmodel.IModelObject)
   */
  @Override
  public void setSourceNode( IModelObject node)
  {
    // remove listener from previous parent
    if ( parent != null) 
    {
      parent.removeModelListener( parentListener);
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displayDeselect( parent.getChildren());
    }
    
    // set parent
    parent = node;
    
    // add listener to new parent
    if ( parent != null) 
    {
      parent.addModelListener( parentListener);
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displaySelect( parent.getChildren());
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setMode(org.xidget.ifeature.model.ISelectionModelFeature.Mode)
   */
  @Override
  public void setMode( Mode mode)
  {
    this.mode = mode;
    switch( mode)
    {
      case ref: fkMap = null; break;
      case fk1:
      case fk2: fkMap = new HashMap<Object, Object>(); break;
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#select(java.lang.Object)
   */
  @Override
  public void select( Object object)
  {
    if ( fkMap != null)
    {
      switch( mode)
      {
        case ref: break;
        case fk1: fkMap.put( ((IModelObject)object).getValue(), object); break;
        case fk2: fkMap.put( ((IModelObject)object).getID(), object); break;
      }
    }
    
    if ( varName != null)
    {
      StatefulContext context = getContext();
      if ( context != null) context.getScope().insert( varName, object);
    }
    
    if ( parent != null && object instanceof IModelObject)
    {
      parent.addChild( createReference( (IModelObject)object));
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#deselect(java.lang.Object)
   */
  @Override
  public void deselect( Object object)
  {
    if ( fkMap != null)
    {
      switch( mode)
      {
        case ref: break;
        case fk1: fkMap.remove( ((IModelObject)object).getValue()); break;
        case fk2: fkMap.remove( ((IModelObject)object).getID()); break;
      }
    }
    
    if ( varName != null)
    {
      StatefulContext context = getContext();
      if ( context != null) context.getScope().remove( varName, object);
    }
    
    if ( parent != null && object instanceof IModelObject)
    {
      IModelObject element = (IModelObject)object;
      switch( mode)
      {
        case ref:
        {
          parent.removeChild( element); 
          break;
        }
        
        case fk1:
        {
          for( IModelObject child: parent.getChildren( element.getType()))
          {
            Object value1 = child.getValue();
            Object value2 = element.getValue();
            if ( (value1 == null || value2 == null && value1 == value2) || value1.equals( value2))
            {
              child.removeFromParent();
            }
          }
          break;
        }
        
        case fk2:
        {
          for( IModelObject child: parent.getChildren( element.getType()))
          {
            if ( child.getID().equals( element.getID()))
              child.removeFromParent();
          }
          break;
        }
      }
    }
  }
  
  /**
   * Returns an appropriate reference to the specified element.
   * @param element The element.
   * @return Returns an appropriate reference to the specified element.
   */
  private IModelObject createReference( IModelObject element)
  {
    switch( mode)
    {
      case ref: return new Reference( element);
        
      case fk1:
      {
        IModelObject fk = element.createObject( element.getType());
        fk.setValue( element.getID());
        return fk;
      }
        
      case fk2:
      {
        IModelObject fk = element.createObject( element.getType());
        fk.setID( element.getID());
        return fk;
      }
    }

    throw new IllegalStateException();
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#setSelection(java.util.List)
   */
  @Override
  public void setSelection( List<? extends Object> list)
  {
    if ( list == null) list = Collections.emptyList();
    
    if ( varName != null)
    {
      StatefulContext context = getContext();
      if ( context != null) context.getScope().set( varName, list);
    }
    
    if ( parent != null)
    {
      parent.removeChildren();
      for( Object object: list)
      {
        parent.addChild( new Reference( (IModelObject)object));
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISelectionModelFeature#getSelection()
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<? extends Object> getSelection()
  {
    List<? extends Object> inList = null;
        
    StatefulContext context = getContext();
    if ( context != null && varName != null) inList = (List<? extends Object>)context.getScope().get( varName);
    else if ( parent != null) inList = parent.getChildren();
    
    if ( inList != null)
    {
      List<IModelObject> outList = new ArrayList<IModelObject>( inList.size());
      switch( mode)
      {
        case ref:
        {
          for( Object object: inList)
          {
            outList.add( ((IModelObject)object).getReferent());
          }
          break;
        }
        
        case fk1:
        {
          for( Object object: inList)
          {
            outList.add( (IModelObject)fkMap.get( ((IModelObject)object).getValue()));
          }
          break;
        }
        
        case fk2:
        {
          for( Object object: inList)
          {
            outList.add( (IModelObject)fkMap.get( ((IModelObject)object).getID()));
          }
        }
      }
      
      return outList;
    }
    
    return Collections.emptyList();
  }

  /**
   * @return Returns the context for script execution and/or transformation.
   */
  protected StatefulContext getContext()
  {
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    return bindFeature.getBoundContext();
  }

  private IExpressionListener variableListener = new ExpressionListener() {
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displaySelect( nodes);
    }
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displayDeselect( nodes);
    }
  };

  private IModelListener parentListener = new ModelListener() {
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displaySelect( Collections.singletonList( child));
    }
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      ISelectionUpdateFeature feature = xidget.getFeature( ISelectionUpdateFeature.class);
      feature.displayDeselect( Collections.singletonList( child));
    }
  };

  protected IXidget xidget;
  private Mode mode;
  private String varName;
  private IExpression varExpr;
  private IModelObject parent;
  private Map<Object, Object> fkMap;
}
