/**
 * Xidget - UI Toolkit based on XModel
 * Copyright 2009 Bob Dunnagan. All rights reserved.
 */
package org.xidget.binding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xidget.IXidget;
import org.xidget.chart.Point;
import org.xidget.config.ITagHandler;
import org.xidget.config.TagException;
import org.xidget.config.TagProcessor;
import org.xidget.config.ifeature.IXidgetFeature;
import org.xidget.ifeature.IBindFeature;
import org.xidget.ifeature.chart.IPointsFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.listeners.ListDetailListener;
import org.xmodel.listeners.NodeValueListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;

/**
 * An implementation of ITagHandler for the points element.
 */
public class PointsTagHandler implements ITagHandler
{
  public PointsTagHandler()
  {
    map = new HashMap<IModelObject, Point>();
  }
  
  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#enter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean enter( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
    IXidgetFeature xidgetFeature = parent.getFeature( IXidgetFeature.class);
    if ( xidgetFeature == null) throw new TagException( "Parent tag handler must have an IXidgetFeature.");
    
    IXidget xidget = xidgetFeature.getXidget();
    IExpression expression = Xlate.childGet( element, "list", (IExpression)null);
    if ( expression != null)
    {
      IExpression[] coordExprs = createCoordinateExpressions( element);
      PointNodeListener listener = new PointNodeListener( xidget, coordExprs.length);

      for( int i=0; i<coordExprs.length; i++)
      {
        if ( coordExprs[ i] != null) 
        {
          listener.addDetail( coordExprs[ i], new CoordListener( xidget, i));
        }
      }
      
      IExpression labelExpr = Xlate.childGet( element, "label", (IExpression)null);
      if ( labelExpr != null) listener.addDetail( labelExpr, new LabelListener( xidget));
      
      IExpression foregroundExpr = Xlate.childGet( element, "foreground", (IExpression)null);
      if ( foregroundExpr != null) listener.addDetail( foregroundExpr, new ForegroundColorListener( xidget));
      
      IExpression backgroundExpr = Xlate.childGet( element, "background", (IExpression)null);
      if ( backgroundExpr != null) listener.addDetail( backgroundExpr, new BackgroundColorListener( xidget));
      
      XidgetBinding binding = new XidgetBinding( expression, listener);
      IBindFeature bindFeature = xidget.getFeature( IBindFeature.class);
      bindFeature.addBindingAfterChildren( binding);
    }
    
    return false;
  }
  
  /**
   * Create the coordinate expressions from the children of the specified element.
   * @param element The element.
   * @return Returns the coordinate expressions.
   */
  private IExpression[] createCoordinateExpressions( IModelObject element)
  {
    List<IModelObject> children = element.getChildren( "coord");
    IExpression[] result = new IExpression[ children.size()];
    int i=0;
    for( IModelObject child: children)
    {
      result[ i++] = Xlate.get( child, "source", Xlate.childGet( child, "source", (IExpression)null));
    }
    return result;
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#exit(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public void exit( TagProcessor processor, ITagHandler parent, IModelObject element) throws TagException
  {
  }

  /* (non-Javadoc)
   * @see org.xidget.config.ITagHandler#filter(org.xidget.config.TagProcessor, org.xidget.config.ITagHandler, org.xmodel.IModelObject)
   */
  @Override
  public boolean filter( TagProcessor processor, ITagHandler parent, IModelObject element)
  {
    return true;
  }

  /* (non-Javadoc)
   * @see org.xidget.IFeatured#getFeature(java.lang.Class)
   */
  @Override
  public <T> T getFeature( Class<T> clss)
  {
    return null;
  }
  
  private Map<IModelObject, Point> map;
  
  private class PointNodeListener extends ListDetailListener
  {
    public PointNodeListener( IXidget xidget, int dimensions)
    {
      this.xidget = xidget;
      this.dimensions = dimensions;
    }

    @Override
    protected void notifyDetailsBound( IContext context, int index)
    {
      Point point = map.get( context.getObject());
      IPointsFeature feature = xidget.getFeature( IPointsFeature.class);
      feature.add( index, point);
    }

    /* (non-Javadoc)
     * @see org.xmodel.listeners.ListDetailListener#notifyInsert(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyInsert( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      for( int i=0; i<count; i++) 
      {
        Point point = new Point();
        point.coords = new double[ dimensions];
        IModelObject node = nodes.get( start + i);
        map.put( node, point);
      }
      
      super.notifyInsert( expression, context, nodes, start, count);
    }

    /* (non-Javadoc)
     * @see org.xmodel.listeners.ListDetailListener#notifyRemove(org.xmodel.xpath.expression.IExpression, org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      super.notifyRemove( expression, context, nodes, start, count);
      
      IPointsFeature feature = xidget.getFeature( IPointsFeature.class);
      for( int i=0; i<count; i++) 
      {
        map.remove( nodes.get( start + i));
        feature.remove( start);
      }
    }
    
    private IXidget xidget;
    private int dimensions;
  }
  
  private class CoordListener extends NodeValueListener
  {
    public CoordListener( IXidget xidget, int coordinate)
    {
      this.xidget = xidget;
      this.coordinate = coordinate;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      Point point = map.get( context.getObject());
      if ( point == null) return;
      
      point.coords[ coordinate] = (Double)newValue;
      
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      feature.updatePoint( point);
    }
    
    private IXidget xidget;
    private IPointsFeature feature;
    private int coordinate;
  }
  
  private class LabelListener extends NodeValueListener
  {
    public LabelListener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      Point point = map.get( context.getObject());
      if ( point == null) return;
      
      point.label = newValue.toString();
      
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      feature.updatePoint( point);
    }
    
    private IXidget xidget;
    private IPointsFeature feature;
  }
  
  private class ForegroundColorListener extends NodeValueListener
  {
    public ForegroundColorListener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      Point point = map.get( context.getObject());
      if ( point == null) return;
      
      point.foreground = newValue.toString();
      
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      feature.updatePoint( point);
    }
    
    private IXidget xidget;
    private IPointsFeature feature;
  }
  
  private class BackgroundColorListener extends NodeValueListener
  {
    public BackgroundColorListener( IXidget xidget)
    {
      this.xidget = xidget;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.listeners.NodeValueListener#notifyValue(org.xmodel.xpath.expression.IContext, java.lang.Object, java.lang.Object)
     */
    @Override
    protected void notifyValue( IContext context, Object newValue, Object oldValue)
    {
      Point point = map.get( context.getObject());
      if ( point == null) return;
      
      point.background = newValue.toString();
      
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      feature.updatePoint( point);
    }
    
    private IXidget xidget;
    private IPointsFeature feature;
  }
}
