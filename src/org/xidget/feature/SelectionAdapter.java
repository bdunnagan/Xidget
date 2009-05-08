/*
 * Stonewall Networks, Inc.
 *
 *   Project: XModelUIPlugin
 *   Author:  bdunnagan
 *   Date:    Jun 9, 2006
 *
 * Copyright 2006.  Stonewall Networks, Inc.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.List;
import org.xmodel.ChangeSet;
import org.xmodel.IBoundChangeRecord;
import org.xmodel.IChangeRecord;
import org.xmodel.IChangeSet;
import org.xmodel.IModelObject;
import org.xmodel.ModelObject;
import org.xmodel.Reference;
import org.xmodel.Xlate;
import org.xmodel.diff.DefaultXmlMatcher;
import org.xmodel.diff.IXmlDiffer;
import org.xmodel.diff.IXmlMatcher;
import org.xmodel.diff.ShallowDiffer;
import org.xmodel.diff.XmlDiffer;
import org.xmodel.external.IExternalReference;
import org.xmodel.xaction.IXAction;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.Context;
import org.xmodel.xpath.expression.ExpressionException;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.IExpressionListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * 
 */
public abstract class SelectionFeature
{
  public SelectionFeature()
  {
    listening = true;
  }
  
  /**
   * Configure the adapter from the view-model.
   * @param viewModel The view-model.
   */
  private void configure( ViewModel viewModel) throws ActivationException
  {
    // get selection parent expression
    selectionExpr = viewModel.getExpression( "parent", false);
    if ( selectionExpr == null)
      throw new ActivationException( "Selection expression is missing.");

    // get selection filter expression
    filterExpr = viewModel.getExpression( "filter", false);
    
    // get selection type
    IModelObject viewRoot = viewModel.getRoot();
    String type = Xlate.get( viewRoot, "type", "ref");
    if ( type.equals( "fk1") || type.equals( "fkey")) this.type = Type.FOREIGN_KEY1;
    if ( type.equals( "fk2")) this.type = Type.FOREIGN_KEY2;
    if ( type.equals( "ref")) this.type = Type.REFERENCE;
    
    // get open action
    doubleClickAction = viewModel.createChildScript( "doubleClick");
  }
  
  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.XmAdapter#activate()
   */
  @Override
  public void activate() throws ActivationException
  {
    if ( isActive()) return;
    configure( getViewModel());
    super.activate();
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.XmAdapter#install(org.xmodel.xpath.expression.IContext)
   */
  @Override
  protected void install( IContext context) throws ExpressionException
  {
    if ( selectionExpr != null) selectionExpr.addNotifyListener( context, targetListener);
  }

  /* (non-Javadoc)
   * @see dunnagan.bob.xmodel.ui.XmAdapter#uninstall(org.xmodel.xpath.expression.IContext, boolean)
   */
  @Override
  protected void uninstall( IContext context, boolean notify) throws ExpressionException
  {
    if ( selectionExpr != null) 
    {
      if ( notify)
      {
        selectionExpr.removeNotifyListener( context, targetListener);
      }
      else
      {
        if ( selectionParent != null) referenceExpr.removeListener( new Context( context, selectionParent), referenceListener);        
        selectionExpr.removeListener( context, targetListener);
      }
      selectionParent = null;
    }
  }

  /**
   * Returns the selection reference (or foreign key stub) for the specified selected object.
   * @param object The selected object.
   * @return Returns the selection reference for the selected object.
   */
  protected IModelObject findReference( IModelObject object)
  {
    if ( type == null || selectionParent == null) return null;
    switch( type)
    {
      case REFERENCE:   return selectionParent.getChild( object.getType(), object.getID());
      case FOREIGN_KEY1: 
      {
        foreignKey1Finder.setVariable( "id", object.getID());
        return foreignKey1Finder.queryFirst( selectionParent);
      }
      case FOREIGN_KEY2:
      {
        foreignKey2Finder.setVariable( "id", object.getID());
        return foreignKey2Finder.queryFirst( selectionParent);
      }
    }
    return null;
  }
  
  /**
   * Returns the selected object which corresponds to the selection reference.
   * @param reference The selection reference.
   * @return Returns the selected object which corresponds to the selection reference.
   */
  protected IModelObject findReferent( IModelObject reference)
  {
    if ( type == null || selectionParent == null) return null;
    List<IModelObject> sources = getSelectionSources();
    switch( type)
    {
      case REFERENCE: 
      {
        IModelObject referent = reference.getReferent();
        if ( sources.contains( referent)) return referent;
      }
      
      case FOREIGN_KEY1: 
      {
        String id = Xlate.get( reference, "");
        for( IModelObject source: sources)
          if ( source.getID().equals( id))
            return source;
      }

      case FOREIGN_KEY2: 
      {
        String id = Xlate.get( reference, "id", "");
        for( IModelObject source: sources)
          if ( source.getID().equals( id))
            return source;
      }
    }  
    
    return null;
  }
  
  /**
   * Create a selection reference for the specified selected object.
   * @param selection The selected object.
   * @return Returns the new selection reference.
   */
  protected IModelObject createReference( IModelObject selection)
  {
    switch( type)
    {
      case REFERENCE: return new Reference( selection);
      
      case FOREIGN_KEY1:
      {
        IModelObject reference = new ModelObject( selection.getType());
        reference.setValue( selection.getID());
        return reference;
      }

      case FOREIGN_KEY2:
      {
        IModelObject reference = new ModelObject( selection.getType());
        reference.setID( selection.getID());
        return reference;
      }
    }
    
    return null;
  }
  
  /**
   * Add a selection reference for the specified selected object.
   * @param object The selected object.
   */
  public void addSelectionReference( IModelObject object)
  {
    IModelObject reference = createReference( object);
    if ( reference != null) selectionParent.addChild( reference);
  }
  
  /**
   * Remove the selection reference for the specified selected object.
   * @param object The selection.
   */
  public void removeSelectionReference( IModelObject object)
  {
    IModelObject reference = findReference( object);
    if ( reference != null) reference.removeFromParent();
  }
  
  /**
   * Returns the double-click action.
   * @return Returns the double-click action.
   */
  public IXAction getDoubleClickAction()
  {
    return doubleClickAction;
  }
  
  /**
   * Returns true if the specified object is selected.
   * @param object The object to be tested.
   * @return Returns true if the specified object is selected.
   */
  public boolean isSelected( IModelObject object)
  {
    return findReference( object) != null;
  }
  
  /**
   * Update the selection references to correspond with the specified list of selected objects.
   * This method creates a new list of references and diffs them with the selection parent.
   * @param selection The list of selected objects.
   */
  public void updateSelectionReferences( List<IModelObject> selection)
  {
    if ( selectionParent == null) return;
    
    // create new reference list and set of reference types
    List<IModelObject> newReferences = new ArrayList<IModelObject>();
    IModelObject dummyParent = selectionParent.cloneObject();
    for( IModelObject selected: selection)
    {
      IModelObject reference = createReference( selected);
      if ( reference != null && isReference( reference)) 
      {
        newReferences.add( reference);
        dummyParent.addChild( reference);
      }
    }

    // diff
    switch( type)
    {
      case REFERENCE:
      {
        IChangeSet changeSet = new ChangeSet();
        ShallowDiffer differ = new ShallowDiffer();
        differ.setMatcher( matcher);
        differ.diff( selectionParent, dummyParent, changeSet);
        
        // only apply changes involving reference types
        for( IBoundChangeRecord record: changeSet.getRecords())
        {
          IModelObject object = record.getBoundObject();
          switch( record.getType())
          {
            case IChangeRecord.REMOVE_CHILD:
            {
              int index = record.getIndex();
              IModelObject child = (index >= 0)? record.getChild(): object.getChild( index);
              if ( !isReference( child)) continue;
            }
            break;
            
            case IChangeRecord.CHANGE_ATTRIBUTE:
            {
              if ( !isReference( object)) continue;
            }
            break;
          }
          record.applyChange();
        }
      }
      break;
      
      case FOREIGN_KEY1:
      case FOREIGN_KEY2:
      {
        IChangeSet changeSet = new ChangeSet();
        IXmlDiffer differ = new XmlDiffer();
        differ.diff( selectionParent, dummyParent, changeSet);

        // only apply changes involving reference types
        for( IBoundChangeRecord record: changeSet.getRecords())
        {
          IModelObject object = record.getBoundObject();
          switch( record.getType())
          {
            case IChangeRecord.REMOVE_CHILD:
            {
              int index = record.getIndex();
              IModelObject child = (index >= 0)? record.getChild(): object.getChild( index);
              if ( !isReference( child)) continue;
            }
            break;
            
            case IChangeRecord.CHANGE_ATTRIBUTE:
            {
              if ( !isReference( object)) continue;
            }
            break;
          }
          record.applyChange();
        }
      }
      break;
    }
  }
  
  /**
   * Returns true if the filter expression is satisifed by the specified object.
   * @param object The object.
   * @return Returns true if the filter expression is satisifed by the specified object.
   */
  private boolean isReference( IModelObject object)
  {
    StatefulContext context = new StatefulContext( getContext(), object);
    context.set( "v", object);
    return (filterExpr == null || filterExpr.evaluateBoolean( context, true));
  }
  
  /**
   * Returns the list of selected objects (from the widget).
   * @return Returns the list of selected objects.
   */
  protected abstract List<IModelObject> getSelectionSources();
  
  /**
   * Update the selection in the widget corresponding to the specified selection reference.
   * This method is called when the datamodel is updated and the widget should programmatically
   * select the corresponding row in its list or table.
   * @param proto An element for which a best match will be found.
   */
  protected abstract void selectObject( IModelObject proto);

  /**
   * Update the selection in the widget corresponding to the specified selection reference.
   * This method is called when the datamodel is updated and the widget should programmatically
   * deselect the corresponding row in its list or table.
   * @param proto An element for which a best match will be found.
   */
  protected abstract void deselectObject( IModelObject proto);
    
  final IExpressionListener targetListener = new ExpressionListener() {
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      update( expression, context);
    }
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      update( expression, context);
    }
    private void update( IExpression expression, IContext context)
    {
      List<IModelObject> list = expression.query( context, null);
      IModelObject newTarget = (list.size() > 0)? list.get( 0): null;
      
      // sanity check the use of Reference objects
      if ( newTarget != null && newTarget instanceof IExternalReference && type == Type.REFERENCE)
      {
        log.error( "Unable to set selection parent to IExternalReference when type = 'ref'.");
        return;
      }
      
      // install new selection parent
      if ( selectionParent != newTarget)
      {
        // update reference listener
        if ( selectionParent != null) referenceExpr.removeNotifyListener( new Context( context, selectionParent), referenceListener);
        if ( newTarget != null) referenceExpr.addNotifyListener( new Context( context, newTarget), referenceListener);

        // change parent
        selectionParent = newTarget;
      }
    }
  };
                                                                      
  final IExpressionListener referenceListener = new ExpressionListener() {
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( !listening) return;
      listening = false;
      try
      {
        for( IModelObject node: nodes) selectObject( node);
      }
      finally
      {
        listening = true;
      }
    }
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      if ( !listening) return;
      listening = false;
      try
      {
        for( IModelObject node: nodes) deselectObject( node);
      }
      finally
      {
        listening = true;
      }
    }
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( !listening) return;
      listening = false;
      try
      {
        if ( oldValue != null) deselectObject( object);
        if ( newValue != null) selectObject( object);
      }
      finally
      {
        listening = true;
      }
    }
  };
  
  private final IXmlMatcher matcher = new DefaultXmlMatcher() {
    public boolean isMatch( IModelObject localChild, IModelObject foreignChild)
    {
      if ( !localChild.isType( foreignChild.getType())) return false;
      
      String localID = localChild.getID();
      String foreignID = foreignChild.getID();
      if ( localID.length() > 0 || foreignID.length() > 0)
        return super.isMatch( localChild, foreignChild);
      
      return localChild.getReferent() == foreignChild.getReferent();
    }
  };
  
  final IExpression foreignKey1Finder = XPath.createExpression( "*[ . = $id]");
  final IExpression foreignKey2Finder = XPath.createExpression( "*[ @id = $id]");
  final IExpression referenceExpr = XPath.createExpression( "*");
  
  protected enum Type { FOREIGN_KEY1, FOREIGN_KEY2, REFERENCE};

  private IExpression selectionExpr;
  private IExpression filterExpr;
  private IModelObject selectionParent;
  private boolean listening = true;
  private Type type;
  private IXAction doubleClickAction;
}
