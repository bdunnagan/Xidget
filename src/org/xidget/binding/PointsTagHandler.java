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
import org.xidget.ifeature.IPointsFeature;
import org.xmodel.IModelObject;
import org.xmodel.Xlate;
import org.xmodel.xpath.expression.ExactExpressionListener;
import org.xmodel.xpath.expression.ExpressionListener;
import org.xmodel.xpath.expression.IContext;
import org.xmodel.xpath.expression.IExpression;
import org.xmodel.xpath.expression.StatefulContext;

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
      IExpression labelExpr = Xlate.childGet( element, "label", (IExpression)null);
      IExpression fgColorExpr = Xlate.childGet( element, "fgcolor", (IExpression)null);
      IExpression bgColorExpr = Xlate.childGet( element, "bgcolor", (IExpression)null);
      
      PointNodeListener listener = new PointNodeListener( xidget, createCoordinateExpressions( element), labelExpr, fgColorExpr, bgColorExpr);
      
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
      result[ i++] = Xlate.get( child, (IExpression)null);
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
  
  private class PointNodeListener extends ExactExpressionListener
  {
    public PointNodeListener( IXidget xidget, IExpression[] coordExprs, IExpression labelExpr, IExpression fgColorExpr, IExpression bgColorExpr)
    {
      this.xidget = xidget;
      this.coordExprs = coordExprs;
      this.labelExpr = labelExpr;
      this.fgColorExpr = fgColorExpr;
      this.bgColorExpr = bgColorExpr;
      
      coordListeners = new PointCoordListener[ coordExprs.length];
      for( int i=0; i<coordListeners.length; i++)
      {
        coordListeners[ i] = new PointCoordListener( xidget, i);
      }
      
      labelListener = new PointDetailListener( xidget, Detail.label);
      fgColorListener = new PointDetailListener( xidget, Detail.fgcolor);
      bgColorListener = new PointDetailListener( xidget, Detail.bgcolor);
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExactExpressionListener#notifyInsert(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyInsert( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      IPointsFeature feature = xidget.getFeature( IPointsFeature.class);
      for( int i=0; i<count; i++) 
      {
        IModelObject node = nodes.get( start + i);
        
        Point point = new Point();
        map.put( node, point);
        
        StatefulContext pointContext = new StatefulContext( context, node);
        point.coords = new double[ coordExprs.length];
        for( int j=0; j<point.coords.length; j++)
        {
          double coord = coordExprs[ j].evaluateNumber( pointContext);
          point.coords[ j] = coord;
          coordExprs[ j].addListener( pointContext, coordListeners[ j]);
        }
        
        if ( labelExpr != null) 
        {
          point.label = labelExpr.evaluateString( pointContext);
          labelExpr.addListener( pointContext, labelListener);
        }
        
        if ( fgColorExpr != null)
        {
          point.fgColor = fgColorExpr.evaluateString( pointContext);
          fgColorExpr.addListener( pointContext, fgColorListener);
        }
        
        if ( bgColorExpr != null)
        {
          point.bgColor = bgColorExpr.evaluateString( pointContext);
          bgColorExpr.addListener( pointContext, bgColorListener);
        }
        
        feature.add( start + i, point);
      }
    }

    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExactExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List, int, int)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes, int start, int count)
    {
      IPointsFeature feature = xidget.getFeature( IPointsFeature.class);
      for( int i=0; i<count; i++) feature.remove( start);
    }
    
    private IXidget xidget;
    private IExpression[] coordExprs;
    private PointCoordListener[] coordListeners;
    private IExpression labelExpr;
    private IExpression fgColorExpr;
    private IExpression bgColorExpr;
    private PointDetailListener labelListener;
    private PointDetailListener fgColorListener;
    private PointDetailListener bgColorListener;
  }
  
  private class PointCoordListener extends ExpressionListener
  {
    public PointCoordListener( IXidget xidget, int coordinate)
    {
      this.xidget = xidget;
      this.coordinate = coordinate;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      update( context, Xlate.get( node, 0.0));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      update( context, Xlate.get( node, 0.0));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      update( context, newValue);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      if ( newValue != null)
      {
        update( context, newValue);
      }
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( newValue != null)
      {
        if ( newValue instanceof Double)
        {
          update( contexts[ 0], (Double)newValue);
        }
        else
        {
          update( contexts[ 0], newValue.toString());
        }
      }
    }
    
    /**
     * Parse a double from the specified string and update the coordinate.
     * @param context The context.
     * @param value The string value.
     */
    private void update( IContext context, String value)
    {
      try { update( context, Double.parseDouble( value));} catch( Exception e) {}
    }
    
    /**
     * Update the coordinate with the specified value.
     * @param context The context.
     * @param value The new value.
     */
    private void update( IContext context, double value)
    {
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      Point point = map.get( context.getObject());
      if ( point == null) return;
      feature.update( point, coordinate, value);
    }
    
    private IXidget xidget;
    private IPointsFeature feature;
    private int coordinate;
  }
  
  public static enum Detail { label, fgcolor, bgcolor};
  
  private class PointDetailListener extends ExpressionListener
  {
    public PointDetailListener( IXidget xidget, Detail detail)
    {
      this.xidget = xidget;
      this.detail = detail;
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyAdd(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyAdd( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      update( context, Xlate.get( node, ""));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyRemove(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.util.List)
     */
    @Override
    public void notifyRemove( IExpression expression, IContext context, List<IModelObject> nodes)
    {
      IModelObject node = expression.queryFirst( context);
      update( context, Xlate.get( node, ""));
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, double, double)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, double newValue, double oldValue)
    {
      update( context, ""+newValue);
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyChange(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext, java.lang.String, java.lang.String)
     */
    @Override
    public void notifyChange( IExpression expression, IContext context, String newValue, String oldValue)
    {
      if ( newValue != null)
      {
        update( context, newValue);
      }
    }
    
    /* (non-Javadoc)
     * @see org.xmodel.xpath.expression.ExpressionListener#notifyValue(org.xmodel.xpath.expression.IExpression, 
     * org.xmodel.xpath.expression.IContext[], org.xmodel.IModelObject, java.lang.Object, java.lang.Object)
     */
    @Override
    public void notifyValue( IExpression expression, IContext[] contexts, IModelObject object, Object newValue, Object oldValue)
    {
      if ( newValue != null) update( contexts[ 0], newValue.toString());
    }
    
    /**
     * Update the detail with the specified value.
     * @param context The context.
     * @param value The new value.
     */
    private void update( IContext context, String value)
    {
      if ( feature == null) feature = xidget.getFeature( IPointsFeature.class);
      Point point = map.get( context.getObject());
      if ( point == null) return;
      
      switch( detail)
      {
        case label:   point.label = value; break;
        case fgcolor: point.fgColor = value; break;
        case bgcolor: point.bgColor = value; break;
      }
      
      feature.update( point, value);
    }
    
    private IXidget xidget;
    private Detail detail;
    private IPointsFeature feature;
  }
}
