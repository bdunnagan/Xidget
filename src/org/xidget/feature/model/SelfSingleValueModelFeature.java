/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.feature.model;

import org.xidget.IXidget;
import org.xidget.ifeature.model.ISingleValueModelFeature;
import org.xidget.ifeature.model.ISingleValueUpdateFeature;
import org.xmodel.IModelObject;
import org.xmodel.diff.DefaultXmlMatcher;
import org.xmodel.diff.XmlDiffer;
import org.xmodel.external.NonSyncingListener;

/**
 * An implementation of ISingleValueModelFeature that passes the storage node as the value
 * instead of the calling the getValue() method on the node and returning its result.
 */
public class SelfSingleValueModelFeature implements ISingleValueModelFeature
{
  public SelfSingleValueModelFeature( IXidget xidget)
  {
    this.xidget = xidget;
    differ = new XmlDiffer( new DefaultXmlMatcher( true));
    listener = new Listener();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setStorageLocation(org.xmodel.IModelObject)
   */
  @Override
  public void setSourceNode( IModelObject newNode)
  {
    IModelObject oldNode = node;
    if ( oldNode == newNode) return;
    
    if ( node != null) listener.uninstall( node);
    if ( newNode != null) listener.install( newNode);
    
    node = newNode;
    
    ISingleValueUpdateFeature updateFeature = xidget.getFeature( ISingleValueUpdateFeature.class);
    updateFeature.display( getValue());
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setSourceVariable(java.lang.String)
   */
  @Override
  public void setSourceVariable( String name)
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#getValue()
   */
  @Override
  public Object getValue()
  {
    return node;
  }

  /* (non-Javadoc)
   * @see org.xidget.ifeature.model.ISingleValueModelFeature#setValue(java.lang.Object)
   */
  @Override
  public void setValue( Object value)
  {
    if ( !(value instanceof IModelObject)) return;
    
    IModelObject rhs = (IModelObject)value;
    differ.diffAndApply( node, rhs);
  }

  private class Listener extends NonSyncingListener
  {
    /* (non-Javadoc)
     * @see org.xmodel.external.NonSyncingListener#notifyAddChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    @Override
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      super.notifyAddChild( parent, child, index);
      
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateWidget();
    }

    /* (non-Javadoc)
     * @see org.xmodel.external.NonSyncingListener#notifyRemoveChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    @Override
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      super.notifyRemoveChild( parent, child, index);
      
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateWidget();
    }

    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyChange(org.xmodel.IModelObject, java.lang.String, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyChange( IModelObject object, String attrName, Object newValue, Object oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateWidget();
    }

    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyClear(org.xmodel.IModelObject, java.lang.String, java.lang.Object)
     */
    @Override
    public void notifyClear( IModelObject object, String attrName, Object oldValue)
    {
      ISingleValueUpdateFeature feature = xidget.getFeature( ISingleValueUpdateFeature.class);
      feature.updateWidget();
    }
  }
  
  private IXidget xidget;
  private IModelObject node;
  private XmlDiffer differ;
  private Listener listener;
}
