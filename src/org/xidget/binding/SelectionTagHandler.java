/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.ISelectionModelFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITagHandler for the <i>selection</i> element.
 */
public class SelectionTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature feature = parent.getFeature( IXidgetFeature.class);
    if ( feature == null) return false;
    
    IXidget xidget = feature.getXidget();
    if ( xidget == null) return false;
    
    return xidget.getFeature( ISelectionModelFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    
    // get parent expression
    IExpression expression = Xlate.childGet( element, "parent", Xlate.get( element, "parent", (IExpression)null));
    if ( expression == null) throw new TagException( "Error in parent expression.");
    
    // create binding
    XidgetBinding binding = new XidgetBinding( expression, new Listener( xidget));
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.addBindingAfterChildren( binding);
    
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
  
  private final static class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject selectionParent = expression.queryFirst( context);
      ISelectionModelFeature feature = xidget.getFeature( ISelectionModelFeature.class);
      feature.setParent( (StatefulContext)context, selectionParent);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject selectionParent = expression.queryFirst( context);
      ISelectionModelFeature feature = xidget.getFeature( ISelectionModelFeature.class);
      feature.setParent( (StatefulContext)context, selectionParent);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    private IXidget xidget;
  }
}
