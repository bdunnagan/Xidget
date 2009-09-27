/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.Collections;
import java.util.List;
import org.xidget.IXidget;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.IDynamicContainerFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.XPath;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of ITagHandler for the children expression of a dynamic container.
 */
public class ChildrenTagHandler implements ITagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) return false;
    
    return xidgetFeature.getXidget().getFeature( IDynamicContainerFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");

    IXidget xidget = xidgetFeature.getXidget();
    
    // create expression
    String xpath = Xlate.get( element, "");
    if ( xpath.length() == 0) return false;
    
    // create binding
    IExpression expression = XPath.createExpression( xpath);
    Binding binding = new Binding( xidget, expression);
    IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
    bindFeature.addBindingBeforeChildren( binding); 
    
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

  private final static class Binding extends ExpressionListener implements IXidgetBinding
  {
    public Binding( IXidget xidget, IExpression expression)
    {
      this.xidget = xidget;
      this.expression = expression;
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IDynamicContainerFeature feature = xidget.getFeature( IDynamicContainerFeature.class);
      feature.setChildren( (StatefulContext)context, nodes);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      nodes = expression.query( context, null);
      IDynamicContainerFeature feature = xidget.getFeature( IDynamicContainerFeature.class);
      feature.setChildren( (StatefulContext)context, nodes);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    public void bind( StatefulContext context)
    {
      expression.addNotifyListener( context, this);
    }

    public void unbind( StatefulContext context)
    {
      IDynamicContainerFeature feature = xidget.getFeature( IDynamicContainerFeature.class);
      feature.setChildren( (StatefulContext)context, Collections.<IModelObject>emptyList());
      expression.removeListener( context, this);
    }

    private IXidget xidget;
    private IExpression expression;
  }
}
