/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.xidget.ConfigurationSwitch;
import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.Log;
import org.xidget.ConfigurationSwitch.IListener;
import org.xidget.config.TagException;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IDynamicContainerFeature;
import org.xidget.ifeature.IErrorFeature;
import org.xidget.ifeature.IWidgetContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.diff.AbstractListDiffer;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IDynamicContainerFeature.
 */
public class DynamicContainerFeature implements IDynamicContainerFeature, IListener<IModelObject>
{
  public DynamicContainerFeature( IXidget xidget)
  {
    this.xidget = xidget;
    this.list = new ArrayList<Child>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDynamicContainerFeature#setChildren(org.xmodel.xpath.expression.StatefulContext, java.util.List)
   */
  public void setChildren( StatefulContext context, List<IModelObject> nodes)
  {
    ConfigurationSwitch<IModelObject> configSwitch = getSwitch();
    
    // find changes
    Differ differ = new Differ();
    differ.diff( list, nodes);
    for( Change change: differ.getChanges())
    {
      if ( change.rIndex >= 0)
      {
        for( int i=0; i<change.count; i++)
        {
          Child child = new Child();
          child.context = new StatefulContext( context, nodes.get( change.rIndex + i)); 
          
          // insert in list first so listener can determine index
          list.add( change.lIndex + i, child);
          
          // bind switch to new context
          configSwitch.bind( child.context);
        }
      }
      else
      {
        for( int i=0; i<change.count; i++)
        {
          Child child = list.get( change.lIndex);
          configSwitch.unbind( child.context);
          list.remove( change.lIndex);
        }        
      }
    }
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.IDynamicContainerFeature#getChildren()
   */
  public List<IModelObject> getChildren()
  {
    List<IModelObject> children = new ArrayList<IModelObject>( list.size());
    for( Child child: list) children.add( child.context.getObject());
    return children;
  }

  /* (non-Javadoc)
   * @see org.xidget.ConfigurationSwitch.IListener#notifyMatch(org.xmodel.xpath.expression.StatefulContext, java.lang.Object)
   */
  public void notifyMatch( StatefulContext context, IModelObject config)
  {
    int index = findChild( context);
    assert( index >= 0);
    
    IModelObject clone = config.cloneTree();
    clone.removeAttribute( "when");
    clone.removeChildren( "when");
    
    try
    {
      Child child = list.get( index);
      child.config = new StatefulContext( context, clone);
      List<IXidget> xidgets = Creator.getInstance().create( null, child.config);
      if ( xidgets.size() > 0)
      {
        IXidget childXidget = xidgets.get( 0);
        
        // insert child beneath parent xidget
        xidget.addChild( index, childXidget);

        // insert created xidget
        IWidgetContainerFeature containerFeature = xidget.getFeature( IWidgetContainerFeature.class);
        if ( containerFeature != null) containerFeature.addWidget( index, childXidget);
        
        // bind
        IBindFeature bindFeature = childXidget.getFeature( IBindFeature.class);
        if ( bindFeature != null) bindFeature.bind( child.context);
      }
    }
    catch( TagException e)
    {
      Log.exception( "xidget", e);
      IErrorFeature errorFeature = xidget.getFeature( IErrorFeature.class);
      if ( errorFeature != null)
      {
        errorFeature.structureError( 
          "Unable to create switched configuration after adding child.\nCheck log.");
      }
    }
  }

  /* (non-Javadoc)
   * @see org.xidget.ConfigurationSwitch.IListener#notifyMismatch(org.xmodel.xpath.expression.StatefulContext, java.lang.Object)
   */
  public void notifyMismatch( StatefulContext context, IModelObject config)
  {
    int index = findChild( context);
    assert( index >= 0);

    IXidget childXidget = xidget.getChildren().get( index);
    Creator.getInstance().destroy( childXidget);
  }
  
  /**
   * Find the index of the child with the specified context.
   * @param context The context.
   * @return Returns -1 or the index.
   */
  private int findChild( StatefulContext context)
  {
    for( int i=0; i<list.size(); i++)
    {
      if ( list.get( i).context == context)
        return i;
    }
    return -1;
  }

  /**
   * Returns the configuration switch for the container. This switch will contain any child xidgets
   * that have a condition expression. The condition expression is defined in the <i>when</i> attribute
   * or element.
   * @return Returns the configuration switch.
   */
  private ConfigurationSwitch<IModelObject> getSwitch()
  {
    if ( configSwitch == null)
    {
      configSwitch = new ConfigurationSwitch<IModelObject>( this);
      for( IModelObject child: xidget.getConfig().getChildren())
      {
        IExpression condition = Xlate.get( child, "when", Xlate.childGet( child, "when", (IExpression)null));
        if ( condition != null) configSwitch.addCase( condition, child);
      }
    }
    
    return configSwitch;
  }
  
  /**
   * An implementation of AbstractListDiffer which calls the createInsertChange
   * and createDeleteChange methods.
   */
  @SuppressWarnings("unchecked")
  private static class Differ extends AbstractListDiffer
  {
    /* (non-Javadoc)
     * @see org.xmodel.diff.IListDiffer#notifyInsert(java.util.List, int, int, java.util.List, int, int)
     */
    public void notifyInsert( List lhs, int lIndex, int lAdjust, List rhs, int rIndex, int count)
    {
      Change change = new Change();
      change.lIndex = lIndex + lAdjust;
      change.rIndex = rIndex;
      change.count = count;
      
      if ( changes == null) changes = new ArrayList<Change>();
      changes.add( change);
    }

    /* (non-Javadoc)
     * @see org.xmodel.diff.IListDiffer#notifyRemove(java.util.List, int, int, java.util.List, int)
     */
    public void notifyRemove( List lhs, int lIndex, int lAdjust, List rhs, int count)
    {
      Change change = new Change();
      change.lIndex = lIndex + lAdjust;
      change.rIndex = -1;
      change.count = count;
      
      if ( changes == null) changes = new ArrayList<Change>();
      changes.add( change);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.diff.AbstractListDiffer#isMatch(java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean isMatch( Object lhs, Object rhs)
    {
      return rhs.equals( ((Child)lhs).context.getObject());
    }

    /**
     * Returns the changes.
     * @return Returns the changes.
     */
    public List<Change> getChanges()
    {
      if ( changes == null) return Collections.emptyList();
      return changes;
    }
    
    private List<Change> changes;
  }
  
  private static class Change
  {
    public int lIndex;
    public int rIndex;
    public int count;
  }
  
  private static class Child
  {
    StatefulContext context;
    StatefulContext config; 
  }
  
  private IXidget xidget;
  private ConfigurationSwitch<IModelObject> configSwitch;
  private List<Child> list;
}
