/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import org.xidget.Creator;
import org.xidget.IXidget;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IAsyncFeature;
import org.xidget.ifeature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.external.NonSyncingListener;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITagHandler for the <i>dynamic</i> attribute of a form.
 */
public class RebuildFlagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler handler, IModelObject element)
  {
    IModelObject parent = element.getParent();
    return parent != null && parent.isType( "form") && Xlate.get( element, false);
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature feature = parent.getFeature( IXidgetFeature.class);
    if ( feature == null) return false;
    
    IXidget xidget = feature.getXidget();
    if ( xidget == null) return false;
    
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    if ( bindFeature != null) bindFeature.addBindingAfterChildren( new XidgetBinding( xidget));
    
    return false;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  public <T> T getFeature( Class<T> clss)
  {
    return null;
  }
  
  private static class XidgetBinding implements IXidgetBinding
  {
    public XidgetBinding( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#bind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void bind( StatefulContext context)
    {
      Listener listener = new Listener( xidget, context);
      listener.install( xidget.getConfig()); 
    }

    /* (non-Javadoc)
     * @see org.xidget.binding.IXidgetBinding#unbind(org.xmodel.xpath.expression.StatefulContext)
     */
    public void unbind( StatefulContext context)
    {
      Listener listener = new Listener( xidget, context);
      listener.uninstall( xidget.getConfig()); 
    }
    
    private IXidget xidget;
  }
  
  private static class Listener extends NonSyncingListener implements Runnable
  {
    public Listener( IXidget xidget, StatefulContext context)
    {
      this.xidget = xidget;
      this.context = context;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyAddChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    @Override
    public void notifyAddChild( IModelObject parent, IModelObject child, int index)
    {
      IAsyncFeature asyncFeature = xidget.getFeature( IAsyncFeature.class);
      if ( asyncFeature != null) asyncFeature.schedule( this, 500, false, this);
    }

    /* (non-Javadoc)
     * @see org.xmodel.ModelListener#notifyRemoveChild(org.xmodel.IModelObject, org.xmodel.IModelObject, int)
     */
    @Override
    public void notifyRemoveChild( IModelObject parent, IModelObject child, int index)
    {
      IAsyncFeature asyncFeature = xidget.getFeature( IAsyncFeature.class);
      if ( asyncFeature != null) asyncFeature.schedule( this, 500, false, this);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
      try
      {
        uninstall( xidget.getConfig());
        Creator.getInstance().rebuild( xidget);
        install( xidget.getConfig());
      }
      catch( TagException e)
      {
        System.err.println( e);
      }
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object object)
    {
      if ( !(object instanceof Listener)) return false;
      
      Listener listener = (Listener)object;
      return listener.context == context;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
      return getClass().hashCode() + context.hashCode();
    }

    private IXidget xidget;
    private StatefulContext context;
  }
}
