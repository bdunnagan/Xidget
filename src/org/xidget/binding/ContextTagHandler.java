/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.config.AbstractTagHandler;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

/**
 * An implementation of IBindingRule for binding xidgets that define a nested context.
 */
public class ContextTagHandler extends AbstractTagHandler
{
  /* (non-Javadoc)
   * @see org.xidget.config.AbstractTagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return parent.getFeature( IXidgetFeature.class) != null;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  public boolean enter( TagProcessor processor, ITagHandler handler, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = handler.getFeature( IXidgetFeature.class);
    IXidget xidget = xidgetFeature.getXidget();
    if ( xidget == null) return false;
    
    IXidget parent = xidget.getParent();
    if ( parent == null) return false;
    
    IExpression contextExpr = Xlate.get( element, (IExpression)null);
    if ( contextExpr == null) return false;
    
    IBindFeature bindFeature = parent.getFeature( IBindFeature.class);
    XidgetBinding binding = new XidgetBinding( contextExpr, new Listener( xidget));
    bindFeature.addBindingAfterChildren( binding);
    
    return false;
  }

  private static final class Listener extends ExpressionListener
  {
    Listener( IXidget xidget)
    {
      this.xidget = xidget;
      this.map = new HashMap<IContext, StatefulContext>();
    }
    
    private void rebind( IContext context, IModelObject node)
    {
      IBindFeature feature = xidget.getFeature( IBindFeature.class);
      
      StatefulContext nested = map.remove( context);
      if ( nested != null)
      {
        if ( nested.getObject() == node) return;
        feature.unbind( nested, false);
      }
      
      if ( node != null)
      {
        nested = new StatefulContext( context, node);
        map.put( context, nested);
        feature.bind( nested, true);
      }
    }
    
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = nodes.get( 0);
      rebind( context, node);
    }

    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      node = expression.queryFirst( context);
      rebind( context, node);
    }

    public boolean requiresValueNotification()
    {
      return false;
    }

    private IXidget xidget;
    private IModelObject node;
    private Map<IContext, StatefulContext> map;
  }
}
